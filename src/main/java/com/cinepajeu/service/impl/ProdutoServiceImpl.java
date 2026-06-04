package com.cinepajeu.service.impl;

import com.cinepajeu.dto.ProdutoRequestDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.entity.Produto;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.ProdutoRepository;
import com.cinepajeu.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO request) {
        Produto produto = ModelMapper.toEntity(request);
        Produto salvo = produtoRepository.save(produto);
        return ModelMapper.toDto(salvo);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO request) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));

        produto.setNome(request.getNome());
        produto.setCategoria(request.getCategoria());
        produto.setDescricao(request.getDescricao());
        if (request.getUrlImagem() != null) {
            produto.setUrlImagem(request.getUrlImagem());
        }
        produto.setPreco(request.getPreco());
        produto.setQuantidadeEstoque(request.getQuantidadeEstoque());
        produto.setEstoqueMinimo(request.getEstoqueMinimo());
        produto.setAtivo(request.getAtivo());

        Produto atualizado = produtoRepository.save(produto);
        return ModelMapper.toDto(atualizado);
    }

    @Override
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));
        return ModelMapper.toDto(produto);
    }

    @Override
    public Page<ProdutoResponseDTO> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ModelMapper::toDto);
    }

    @Override
    public List<ProdutoResponseDTO> listarEstoqueBaixo() {
        return produtoRepository.findProdutosEstoqueBaixo().stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProdutoResponseDTO> listarEstoqueBaixo(Pageable pageable) {
        return produtoRepository.findProdutosEstoqueBaixo(pageable).map(ModelMapper::toDto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO reporEstoque(Long id, Integer quantidade) {
        if (quantidade <= 0) {
            throw new BusinessException("A quantidade para reposição deve ser maior que zero");
        }
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        Produto salvo = produtoRepository.save(produto);
        return ModelMapper.toDto(salvo);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new BusinessException("Produto não encontrado com o ID: " + id);
        }
        try {
            produtoRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir o produto pois ele possui histórico de vendas");
        }
    }
}
