package com.cinepajeu.util;

import com.cinepajeu.repository.VendaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CodigoPedidoGenerator {

    private static final String PREFIX = "BOM";
    private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final VendaProdutoRepository vendaProdutoRepository;

    public String gerar() {
        String data = LocalDate.now().format(DATE_FMT);
        for (int tentativa = 0; tentativa < 50; tentativa++) {
            String codigo = PREFIX + "-" + data + "-" + sufixoAleatorio(4);
            if (!vendaProdutoRepository.existsByCodigoPedido(codigo)) {
                return codigo;
            }
        }
        throw new IllegalStateException("Não foi possível gerar código único do pedido");
    }

    private static String sufixoAleatorio(int tamanho) {
        StringBuilder sb = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
}
