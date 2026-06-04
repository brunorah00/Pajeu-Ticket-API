package com.cinepajeu.util;

public final class LoginNormalizer {

    private LoginNormalizer() {
    }

    public static String normalize(String login) {
        if (login == null) {
            return null;
        }
        return login.trim().toLowerCase();
    }
}
