package com.cinepajeu.service.impl;

import com.cinepajeu.dto.ItemVendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.entity.ItemVendaProduto;
import com.cinepajeu.entity.Produto;
import com.cinepajeu.entity.VendaProduto;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.ProdutoRepository;
import com.cinepajeu.repository.VendaProdutoRepository;
import com.cinepajeu.service.VendaProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class VendaProdutoServiceImpl implements VendaProdutoService {

    private final VendaProdutoRepository vendaProdutoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public VendaProdutoResponseDTO registrarVenda(VendaProdutoRequestDTO request) {
        VendaProduto venda = VendaProduto.builder()
                .dataVenda(LocalDateTime.now())
                .valorTotal(BigDecimal.ZERO)
                .itens(new ArrayList<>())
                .build();

        BigDecimal valorTotalVenda = BigDecimal.ZERO;

        for (ItemVendaProdutoRequestDTO itemDto : request.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + itemDto.getProdutoId()));

            if (!produto.getAtivo()) {
                throw new BusinessException("O produto '" + produto.getNome() + "' não está ativo para venda");
            }

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto '" + produto.getNome() + 
                        "'. Disponível: " + produto.getQuantidadeEstoque() + ", Solicitado: " + itemDto.getQuantidade());
            }

            // Diminuir estoque automaticamente
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            // Calcular subtotal automaticamente
            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(itemDto.getQuantidade()));

            ItemVendaProduto item = ItemVendaProduto.builder()
                    .vendaProduto(venda)
                    .produto(produto)
                    .quantidade(itemDto.getQuantidade())
                    .precoUnitario(precoUnitario)
                    .subtotal(subtotal)
                    .build();

            venda.getItens().add(item);
            valorTotalVenda = valorTotalVenda.add(subtotal);
        }

        // Calcular total da venda automaticamente
        venda.setValorTotal(valorTotalVenda);

        VendaProduto salva = vendaProdutoRepository.save(venda);
        return ModelMapper.toDto(salva);
    }
}
