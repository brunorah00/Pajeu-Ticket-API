package com.cinepajeu.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class SessaoEncerramento {

    /** Minutos após o horário de início em que a sessão é considerada encerrada. */
    public static final int MINUTOS_APOS_HORARIO = 10;

    private SessaoEncerramento() {
    }

    /**
     * Sessão encerrada quando o horário atual é igual ou posterior a (data + horário + 10 min).
     */
    public static boolean isEncerrada(LocalDate data, LocalTime horario) {
        if (data == null || horario == null) {
            return false;
        }
        LocalDateTime limiteVendas = LocalDateTime.of(data, horario).plusMinutes(MINUTOS_APOS_HORARIO);
        return !LocalDateTime.now().isBefore(limiteVendas);
    }
}
