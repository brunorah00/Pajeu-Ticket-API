package com.cinepajeu.util;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class MapaSala {

    private static final Set<String> ASSENTOS_VALIDOS = buildAssentos();

    private MapaSala() {
    }

    private static Set<String> buildAssentos() {
        Set<String> assentos = new LinkedHashSet<>();
        addRow(assentos, "A", 1, 8, 9, 16);
        addRow(assentos, "B", 2, 8, 9, 15);
        addRow(assentos, "C", 1, 8, 9, 16);
        addRow(assentos, "D", 2, 8, 9, 15);
        addRow(assentos, "E", 1, 8, 9, 16);
        addRow(assentos, "F", 1, 7, 10, 16);
        addRow(assentos, "G", 1, 8, 9, 16);
        addRow(assentos, "H", 1, 8, 9, 16);
        addRow(assentos, "I", 1, 8, 9, 16);
        addRow(assentos, "J", 1, 8, 9, 16);
        addRow(assentos, "K", 1, 7, 10, 16);
        addRow(assentos, "L", 1, 8, 9, 16);
        addRow(assentos, "M", 1, 8, 9, 16);
        addRow(assentos, "N", 2, 8, 9, 15);
        addRow(assentos, "O", 1, 8, 9, 16);
        addRow(assentos, "P", 2, 8, 9, 15);
        return Collections.unmodifiableSet(assentos);
    }

    private static void addRow(Set<String> assentos, String row, int leftFrom, int leftTo, int rightFrom, int rightTo) {
        for (int i = leftFrom; i <= leftTo; i++) {
            assentos.add(row + i);
        }
        for (int i = rightFrom; i <= rightTo; i++) {
            assentos.add(row + i);
        }
    }

    public static boolean isAssentoValido(String codigo) {
        if (codigo == null) {
            return false;
        }
        return ASSENTOS_VALIDOS.contains(codigo.trim().toUpperCase());
    }

    public static Set<String> getAssentosValidos() {
        return ASSENTOS_VALIDOS;
    }

    public static void validarLista(List<String> assentos) {
        if (assentos == null || assentos.isEmpty()) {
            throw new IllegalArgumentException("empty");
        }
        for (String codigo : assentos) {
            if (!isAssentoValido(codigo)) {
                throw new IllegalArgumentException(codigo);
            }
        }
        long distintos = assentos.stream().map(String::toUpperCase).distinct().count();
        if (distintos != assentos.size()) {
            throw new IllegalArgumentException("duplicate");
        }
    }
}
