package com.cinepajeu.service;

import com.cinepajeu.entity.OAuthProvider;

public interface OAuthVerificationService {
    OAuthUserInfo verify(OAuthProvider provider, String token);
}
