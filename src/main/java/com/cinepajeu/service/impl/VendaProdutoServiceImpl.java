package com.cinepajeu.service.impl;

import com.cinepajeu.dto.AtualizarStatusPedidoDTO;
import com.cinepajeu.dto.ItemVendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.entity.ItemVendaProduto;
import com.cinepajeu.entity.Produto;
import com.cinepajeu.entity.StatusPedidoBomboniere;
import com.cinepajeu.entity.Usuario;
import com.cinepajeu.entity.VendaProduto;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.ProdutoRepository;
import com.cinepajeu.repository.VendaProdutoRepository;
import com.cinepajeu.service.VendaProdutoService;
import com.cinepajeu.util.CodigoPedidoGenerator;
import com.cinepajeu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaProdutoServiceImpl implements VendaProdutoService {

    private final VendaProdutoRepository vendaProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final CodigoPedidoGenerator codigoPedidoGenerator;

    @Override
    @Transactional
    public VendaProdutoResponseDTO registrarVenda(VendaProdutoRequestDTO request) {
        Usuario cliente = SecurityUtils.getUsuarioAutenticado();

        VendaProduto venda = VendaProduto.builder()
                .codigoPedido(codigoPedidoGenerator.gerarBomboniere())
                .status(StatusPedidoBomboniere.PENDENTE)
                .cliente(cliente)
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

    @Override
    @Transactional(readOnly = true)
    public List<VendaProdutoResponseDTO> listarPedidos(StatusPedidoBomboniere status) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);
        return vendaProdutoRepository.findPedidosComItens(inicio, fim, status).stream()
                .map(ModelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendaProdutoResponseDTO> listarMeusPedidos() {
        Usuario cliente = SecurityUtils.getUsuarioAutenticado();
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.minusDays(30).atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);
        return vendaProdutoRepository.findPedidosByCliente(cliente.getId(), inicio, fim).stream()
                .map(ModelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public VendaProdutoResponseDTO atualizarStatus(Long id, AtualizarStatusPedidoDTO request) {
        VendaProduto venda = vendaProdutoRepository.findByIdComItens(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado com o ID: " + id));
        venda.setStatus(request.getStatus());
        vendaProdutoRepository.save(venda);
        return ModelMapper.toDto(venda);
    }
}
