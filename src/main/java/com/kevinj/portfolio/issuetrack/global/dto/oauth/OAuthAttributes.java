package com.kevinj.portfolio.issuetrack.global.dto.oauth;

import java.util.Map;

public record OAuthAttributes(
    Map<String, Object> attributes,
    String nameAttributeKey,
    String name,
    String email,
    String picture
) {
    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(
            attributes,
            userNameAttributeName,
            (String) attributes.get("name"),
            (String) attributes.get("email"),
            (String) attributes.get("picture")
        );
    }
}
