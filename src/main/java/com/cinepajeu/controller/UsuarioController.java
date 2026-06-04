package com.cinepajeu.controller;

import com.cinepajeu.dto.FuncionarioRequestDTO;
import com.cinepajeu.dto.RedefinirSenhaFuncionarioDTO;
import com.cinepajeu.dto.UsuarioResponseDTO;
import com.cinepajeu.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gestão de funcionários (admin)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/funcionarios")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar funcionários")
    public ResponseEntity<List<UsuarioResponseDTO>> listarFuncionarios() {
        return ResponseEntity.ok(usuarioService.listarFuncionarios());
    }

    @PostMapping("/funcionarios")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar funcionário")
    public ResponseEntity<UsuarioResponseDTO> cadastrarFuncionario(@Valid @RequestBody FuncionarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarFuncionario(request));
    }

    @DeleteMapping("/funcionarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir funcionário")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Long id) {
        usuarioService.excluirFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/funcionarios/{id}/senha")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Redefinir senha do funcionário")
    public ResponseEntity<Void> redefinirSenhaFuncionario(
            @PathVariable Long id,
            @Valid @RequestBody RedefinirSenhaFuncionarioDTO request) {
        usuarioService.redefinirSenhaFuncionario(id, request);
        return ResponseEntity.noContent().build();
    }
}
