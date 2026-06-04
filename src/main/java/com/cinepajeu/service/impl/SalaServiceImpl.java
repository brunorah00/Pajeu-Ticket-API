package com.cinepajeu.service.impl;

import com.cinepajeu.dto.SalaRequestDTO;
import com.cinepajeu.dto.SalaResponseDTO;
import com.cinepajeu.entity.Sala;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.SalaRepository;
import com.cinepajeu.service.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;

    @Override
    @Transactional
    public SalaResponseDTO cadastrar(SalaRequestDTO request) {
        Sala sala = ModelMapper.toEntity(request);
        Sala salvo = salaRepository.save(sala);
        return ModelMapper.toDto(salvo);
    }

    @Override
    @Transactional
    public SalaResponseDTO atualizar(Long id, SalaRequestDTO request) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sala não encontrada com o ID: " + id));

        sala.setNome(request.getNome());
        sala.setCapacidade(request.getCapacidade());

        Sala atualizado = salaRepository.save(sala);
        return ModelMapper.toDto(atualizado);
    }

    @Override
    public SalaResponseDTO buscarPorId(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sala não encontrada com o ID: " + id));
        return ModelMapper.toDto(sala);
    }

    @Override
    public Page<SalaResponseDTO> listar(Pageable pageable) {
        return salaRepository.findAll(pageable).map(ModelMapper::toDto);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new BusinessException("Sala não encontrada com o ID: " + id);
        }
        try {
            salaRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir a sala pois ela possui sessões vinculadas");
        }
    }
}
