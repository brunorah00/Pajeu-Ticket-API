package com.cinepajeu.service.impl;

import com.cinepajeu.dto.DashboardDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.*;
import com.cinepajeu.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FilmeRepository filmeRepository;
    private final SessaoRepository sessaoRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaIngressoRepository vendaIngressoRepository;
    private final VendaProdutoRepository vendaProdutoRepository;
    private final ItemVendaProdutoRepository itemVendaProdutoRepository;

    @Override
    public DashboardDTO obterDashboard() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 1. Calcular total vendido hoje
        BigDecimal totalIngressosHoje = vendaIngressoRepository.sumValorTotalByDataVendaBetween(startOfDay, endOfDay);
        if (totalIngressosHoje == null) totalIngressosHoje = BigDecimal.ZERO;

        BigDecimal totalProdutosHoje = vendaProdutoRepository.sumValorTotalByDataVendaBetween(startOfDay, endOfDay);
        if (totalProdutosHoje == null) totalProdutosHoje = BigDecimal.ZERO;

        BigDecimal totalVendidoHoje = totalIngressosHoje.add(totalProdutosHoje);

        // 2. Ingressos vendidos hoje
        Long totalIngressosVendidosHoje = vendaIngressoRepository.sumQuantidadeByDataVendaBetween(startOfDay, endOfDay);
        if (totalIngressosVendidosHoje == null) totalIngressosVendidosHoje = 0L;

        // 3. Produtos vendidos hoje
        Long totalProdutosVendidosHoje = itemVendaProdutoRepository.sumQuantidadeByDataVendaBetween(startOfDay, endOfDay);
        if (totalProdutosVendidosHoje == null) totalProdutosVendidosHoje = 0L;

        // 4. Quantidades cadastrais
        Long quantidadeFilmes = filmeRepository.count();
        Long quantidadeSessoes = sessaoRepository.count();
        Long quantidadeProdutos = produtoRepository.count();

        // 5. Estoque completo da bomboniere
        List<ProdutoResponseDTO> estoqueBomboniere = produtoRepository.findAllByOrderByCategoriaAscNomeAsc()
                .stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());

        // 6. Produtos com estoque baixo
        List<ProdutoResponseDTO> produtosComEstoqueBaixo = produtoRepository.findProdutosEstoqueBaixo()
                .stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());

        // 7. Produtos mais vendidos (limitar a 5)
        List<Object[]> queryProdutosMaisVendidos = itemVendaProdutoRepository.findProdutosMaisVendidos(PageRequest.of(0, 5));
        List<DashboardDTO.ProdutoMaisVendidoDTO> produtosMaisVendidos = new ArrayList<>();
        for (Object[] row : queryProdutosMaisVendidos) {
            produtosMaisVendidos.add(DashboardDTO.ProdutoMaisVendidoDTO.builder()
                    .produtoId((Long) row[0])
                    .nome((String) row[1])
                    .quantidadeVendida((Long) row[2])
                    .build());
        }

        // 8. Sessões mais vendidas (limitar a 5)
        List<Object[]> querySessoesMaisVendidas = vendaIngressoRepository.findSessoesMaisVendidas(PageRequest.of(0, 5));
        List<DashboardDTO.SessaoMaisVendidaDTO> sessoesMaisVendidas = new ArrayList<>();
        for (Object[] row : querySessoesMaisVendidas) {
            sessoesMaisVendidas.add(DashboardDTO.SessaoMaisVendidaDTO.builder()
                    .sessaoId((Long) row[0])
                    .filmeTitulo((String) row[1])
                    .quantidadeVendida((Long) row[2])
                    .build());
        }

        return DashboardDTO.builder()
                .totalVendidoHoje(totalVendidoHoje)
                .totalIngressosVendidosHoje(totalIngressosVendidosHoje)
                .totalProdutosVendidosHoje(totalProdutosVendidosHoje)
                .quantidadeFilmes(quantidadeFilmes)
                .quantidadeSessoes(quantidadeSessoes)
                .quantidadeProdutos(quantidadeProdutos)
                .estoqueBomboniere(estoqueBomboniere)
                .produtosComEstoqueBaixo(produtosComEstoqueBaixo)
                .produtosMaisVendidos(produtosMaisVendidos)
                .sessoesMaisVendidas(sessoesMaisVendidas)
                .build();
    }
}
