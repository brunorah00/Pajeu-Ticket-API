package com.cinepajeu.util;

import com.cinepajeu.exception.BusinessException;

import java.util.regex.Pattern;

public final class EmailValidator {

    private static final Pattern EMAIL = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                    + "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
                    + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$"
    );

    private EmailValidator() {
    }

    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        String normalized = normalize(email);
        if (normalized.isEmpty() || normalized.length() > 254) {
            return false;
        }
        if (normalized.startsWith(".") || normalized.endsWith(".") || normalized.contains("..")) {
            return false;
        }
        return EMAIL.matcher(normalized).matches();
    }

    public static String normalize(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }

    public static void requireValid(String email) {
        if (!isValid(email)) {
            throw new BusinessException("E-mail inválido. Informe um endereço válido, ex.: seu@email.com");
        }
    }
}
