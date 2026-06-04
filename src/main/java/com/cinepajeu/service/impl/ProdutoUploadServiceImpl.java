package com.cinepajeu.service.impl;

import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.entity.Produto;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.ProdutoRepository;
import com.cinepajeu.service.ProdutoUploadService;
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
public class ProdutoUploadServiceImpl implements ProdutoUploadService {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of("image/jpeg", "image/png", "image/webp");

    private final ProdutoRepository produtoRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    @Transactional
    public ProdutoResponseDTO uploadImagem(Long produtoId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Arquivo de imagem é obrigatório");
        }

        String contentType = file.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new BusinessException("Formato inválido. Use JPEG, PNG ou WEBP.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + produtoId));

        String ext = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };

        String filename = "produto-" + produtoId + "-" + UUID.randomUUID() + ext;
        Path dir = Paths.get(uploadDir, "produtos").toAbsolutePath().normalize();

        try {
            Files.createDirectories(dir);
            Path destino = dir.resolve(filename);
            file.transferTo(destino.toFile());

            if (produto.getUrlImagem() != null) {
                Path antigo = Paths.get(uploadDir).resolve(produto.getUrlImagem().replaceFirst("^/uploads/", ""));
                try {
                    Files.deleteIfExists(antigo);
                } catch (IOException ignored) {
                    // ignora falha ao remover arquivo antigo
                }
            }

            produto.setUrlImagem("/uploads/produtos/" + filename);
            return ModelMapper.toDto(produtoRepository.save(produto));
        } catch (IOException e) {
            throw new BusinessException("Erro ao salvar imagem: " + e.getMessage());
        }
    }
}
