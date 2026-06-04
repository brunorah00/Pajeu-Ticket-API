package com.cinepajeu.mapper;

import com.cinepajeu.dto.*;
import com.cinepajeu.entity.*;

import java.util.stream.Collectors;

public class ModelMapper {

    public static UsuarioResponseDTO toDto(Usuario entity) {
        if (entity == null) return null;
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .login(entity.getLogin())
                .ultimoAcesso(entity.getUltimoAcesso())
                .role(entity.getRole())
                .build();
    }

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;
        return Usuario.builder()
                .nome(dto.getNome())
                .login(dto.getLogin())
                .senha(dto.getSenha())
                .role(dto.getRole())
                .build();
    }

    public static FilmeResponseDTO toDto(Filme entity) {
        if (entity == null) return null;
        return FilmeResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .genero(entity.getGenero())
                .classificacao(entity.getClassificacao())
                .duracao(entity.getDuracao())
                .sinopse(entity.getSinopse())
                .status(entity.getStatus())
                .urlImagem(entity.getUrlImagem())
                .build();
    }

    public static Filme toEntity(FilmeRequestDTO dto) {
        if (dto == null) return null;
        return Filme.builder()
                .titulo(dto.getTitulo())
                .genero(dto.getGenero())
                .classificacao(dto.getClassificacao())
                .duracao(dto.getDuracao())
                .sinopse(dto.getSinopse())
                .status(dto.getStatus())
                .urlImagem(dto.getUrlImagem())
                .build();
    }

    public static SalaResponseDTO toDto(Sala entity) {
        if (entity == null) return null;
        return SalaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .capacidade(entity.getCapacidade())
                .build();
    }

    public static Sala toEntity(SalaRequestDTO dto) {
        if (dto == null) return null;
        return Sala.builder()
                .nome(dto.getNome())
                .capacidade(dto.getCapacidade())
                .build();
    }

    public static SessaoResponseDTO toDto(Sessao entity) {
        if (entity == null) return null;
        return SessaoResponseDTO.builder()
                .id(entity.getId())
                .filme(toDto(entity.getFilme()))
                .data(entity.getData())
                .horario(entity.getHorario())
                .valorIngresso(entity.getValorIngresso())
                .lugaresDisponiveis(entity.getLugaresDisponiveis())
                .build();
    }

    public static Sessao toEntity(SessaoRequestDTO dto, Filme filme, Sala sala) {
        if (dto == null) return null;
        return Sessao.builder()
                .filme(filme)
                .sala(sala)
                .data(dto.getData())
                .horario(dto.getHorario())
                .valorIngresso(dto.getValorIngresso())
                .lugaresDisponiveis(dto.getLugaresDisponiveis())
                .build();
    }

    public static VendaIngressoResponseDTO toDto(VendaIngresso entity) {
        if (entity == null) return null;
        return VendaIngressoResponseDTO.builder()
                .id(entity.getId())
                .sessao(toDto(entity.getSessao()))
                .quantidade(entity.getQuantidade())
                .valorTotal(entity.getValorTotal())
                .dataVenda(entity.getDataVenda())
                .build();
    }

    public static ProdutoResponseDTO toDto(Produto entity) {
        if (entity == null) return null;
        return ProdutoResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .categoria(entity.getCategoria())
                .descricao(entity.getDescricao())
                .urlImagem(entity.getUrlImagem())
                .preco(entity.getPreco())
                .quantidadeEstoque(entity.getQuantidadeEstoque())
                .estoqueMinimo(entity.getEstoqueMinimo())
                .ativo(entity.getAtivo())
                .build();
    }

    public static Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) return null;
        return Produto.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .urlImagem(dto.getUrlImagem())
                .preco(dto.getPreco())
                .quantidadeEstoque(dto.getQuantidadeEstoque())
                .estoqueMinimo(dto.getEstoqueMinimo())
                .ativo(dto.getAtivo())
                .build();
    }

    public static ItemVendaProdutoResponseDTO toDto(ItemVendaProduto entity) {
        if (entity == null) return null;
        return ItemVendaProdutoResponseDTO.builder()
                .id(entity.getId())
                .produto(toDto(entity.getProduto()))
                .quantidade(entity.getQuantidade())
                .precoUnitario(entity.getPrecoUnitario())
                .subtotal(entity.getSubtotal())
                .build();
    }

    public static VendaProdutoResponseDTO toDto(VendaProduto entity) {
        if (entity == null) return null;
        return VendaProdutoResponseDTO.builder()
                .id(entity.getId())
                .dataVenda(entity.getDataVenda())
                .valorTotal(entity.getValorTotal())
                .itens(entity.getItens() == null ? null : entity.getItens().stream()
                        .map(ModelMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
