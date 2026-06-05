package com.cinepajeu.util;

import com.cinepajeu.entity.Filme;
import com.cinepajeu.entity.Sessao;
import com.cinepajeu.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class SessaoHorarioConflito {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private SessaoHorarioConflito() {
    }

    /**
     * Valida se a nova sessão não sobrepõe outra na mesma data (sala única).
     * Cada sessão ocupa o intervalo [horário, horário + duração do filme em minutos).
     */
    public static void validar(
            Filme novoFilme,
            LocalDate data,
            LocalTime novoHorario,
            List<Sessao> sessoesNoDia,
            Long sessaoIdIgnorar
    ) {
        LocalDateTime novoInicio = LocalDateTime.of(data, novoHorario);
        LocalDateTime novoFim = novoInicio.plusMinutes(novoFilme.getDuracao());

        for (Sessao existente : sessoesNoDia) {
            if (sessaoIdIgnorar != null && sessaoIdIgnorar.equals(existente.getId())) {
                continue;
            }

            Filme filmeExistente = existente.getFilme();
            LocalDateTime existenteInicio = LocalDateTime.of(existente.getData(), existente.getHorario());
            LocalDateTime existenteFim = existenteInicio.plusMinutes(filmeExistente.getDuracao());

            if (novoInicio.isBefore(existenteFim) && novoFim.isAfter(existenteInicio)) {
                throw new BusinessException(montarMensagem(filmeExistente, existente.getHorario(), existenteFim.toLocalTime()));
            }
        }
    }

    private static String montarMensagem(Filme filmeExistente, LocalTime horarioExistente, LocalTime horarioFimExistente) {
        return String.format(
                "Choque de horário: a sessão de \"%s\" às %s ocupa a sala até %s (duração %d min). "
                        + "Escolha um horário após o término desta exibição.",
                filmeExistente.getTitulo(),
                horarioExistente.format(HORA),
                horarioFimExistente.format(HORA),
                filmeExistente.getDuracao()
        );
    }
}
