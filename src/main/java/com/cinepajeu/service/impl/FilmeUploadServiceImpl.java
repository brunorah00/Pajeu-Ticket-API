package com.cinepajeu.service.impl;

import com.cinepajeu.dto.FilmeResponseDTO;
import com.cinepajeu.entity.Filme;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.FilmeRepository;
import com.cinepajeu.service.FilmeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilmeUploadServiceImpl implements FilmeUploadService {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of("image/jpeg", "image/png", "image/webp");

    private final FilmeRepository filmeRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    @Transactional
    public FilmeResponseDTO uploadPoster(Long filmeId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Arquivo de imagem é obrigatório");
        }

        String contentType = file.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new BusinessException("Formato inválido. Use JPEG, PNG ou WEBP.");
        }

        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + filmeId));

        String ext = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };

        String filename = "filme-" + filmeId + "-" + UUID.randomUUID() + ext;
        Path dir = Paths.get(uploadDir, "filmes").toAbsolutePath().normalize();

        try {
            Files.createDirectories(dir);
            Path destino = dir.resolve(filename);
            file.transferTo(destino.toFile());

            if (filme.getUrlImagem() != null) {
                Path antigo = Paths.get(uploadDir).resolve(filme.getUrlImagem().replaceFirst("^/uploads/", ""));
                try {
                    Files.deleteIfExists(antigo);
                } catch (IOException ignored) {
                    // ignora falha ao remover arquivo antigo
                }
            }

            filme.setUrlImagem("/uploads/filmes/" + filename);
            return ModelMapper.toDto(filmeRepository.save(filme));
        } catch (IOException e) {
            throw new BusinessException("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FilmeResponseDTO removerPoster(Long filmeId) {
        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + filmeId));

        if (filme.getUrlImagem() != null) {
            Path antigo = Paths.get(uploadDir).resolve(filme.getUrlImagem().replaceFirst("^/uploads/", ""));
            try {
                Files.deleteIfExists(antigo);
            } catch (IOException ignored) {
                // ignora falha ao remover arquivo
            }
            filme.setUrlImagem(null);
        }

        return ModelMapper.toDto(filmeRepository.save(filme));
    }
}
