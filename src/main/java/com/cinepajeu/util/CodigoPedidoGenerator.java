package com.cinepajeu.util;

import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.repository.VendaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class CodigoPedidoGenerator {

    private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final VendaProdutoRepository vendaProdutoRepository;
    private final VendaIngressoRepository vendaIngressoRepository;

    public String gerarBomboniere() {
        return gerarComPrefixo("BOM", vendaProdutoRepository::existsByCodigoPedido);
    }

    public String gerarIngresso() {
        return gerarComPrefixo("ING", vendaIngressoRepository::existsByCodigoPedido);
    }

    /** @deprecated use {@link #gerarBomboniere()} */
    @Deprecated
    public String gerar() {
        return gerarBomboniere();
    }

    private String gerarComPrefixo(String prefix, Predicate<String> codigoExiste) {
        String data = LocalDate.now().format(DATE_FMT);
        for (int tentativa = 0; tentativa < 50; tentativa++) {
            String codigo = prefix + "-" + data + "-" + sufixoAleatorio(4);
            if (!codigoExiste.test(codigo)) {
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
