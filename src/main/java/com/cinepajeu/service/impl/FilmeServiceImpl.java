package com.cinepajeu.service.impl;

import com.cinepajeu.dto.FilmeRequestDTO;
import com.cinepajeu.dto.FilmeResponseDTO;
import com.cinepajeu.entity.Filme;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.FilmeRepository;
import com.cinepajeu.repository.SessaoRepository;
import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.service.FilmeService;
import com.cinepajeu.service.FilmeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FilmeServiceImpl implements FilmeService {

    private final FilmeRepository filmeRepository;
    private final SessaoRepository sessaoRepository;
    private final VendaIngressoRepository vendaIngressoRepository;
    private final FilmeUploadService filmeUploadService;

    @Override
    @Transactional
    public FilmeResponseDTO cadastrar(FilmeRequestDTO request) {
        Filme filme = ModelMapper.toEntity(request);
        Filme salvo = filmeRepository.save(filme);
        return ModelMapper.toDto(salvo);
    }

    @Override
    @Transactional
    public FilmeResponseDTO atualizar(Long id, FilmeRequestDTO request) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + id));

        filme.setTitulo(request.getTitulo());
        filme.setGenero(request.getGenero());
        filme.setClassificacao(request.getClassificacao());
        filme.setDuracao(request.getDuracao());
        filme.setSinopse(request.getSinopse());
        filme.setStatus(request.getStatus());
        if (request.getUrlImagem() != null) {
            filme.setUrlImagem(request.getUrlImagem());
        }

        Filme atualizado = filmeRepository.save(filme);
        return ModelMapper.toDto(atualizado);
    }

    @Override
    public FilmeResponseDTO buscarPorId(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + id));
        return enriquecerComVendas(ModelMapper.toDto(filme), id);
    }

    @Override
    public Page<FilmeResponseDTO> listar(Pageable pageable) {
        return filmeRepository.findAll(pageable).map(f -> enriquecerComVendas(ModelMapper.toDto(f), f.getId()));
    }

    private FilmeResponseDTO enriquecerComVendas(FilmeResponseDTO dto, Long filmeId) {
        dto.setQuantidadeVendasIngresso(vendaIngressoRepository.countByFilmeId(filmeId));
        return dto;
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + id));

        vendaIngressoRepository.deleteByFilmeId(id);
        sessaoRepository.deleteAll(sessaoRepository.findByFilmeId(id));

        if (filme.getUrlImagem() != null) {
            filmeUploadService.removerPoster(id);
        }

        filmeRepository.delete(filme);
    }
}
