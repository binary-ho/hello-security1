package com.jinho.hellosecurity1.config.oauth.provider;

public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
