package com.kevinj.portfolio.issuetrack.global.dto.oauth;

import java.util.Map;

public record OAuthAttributes(
    Map<String, Object> attributes,
    String nameAttributeKey,
    String name,
    String email,
    String picture
) {
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("github".equals(registrationId)) {
            return ofGithub(userNameAttributeName, attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(
            attributes,
            userNameAttributeName,
            (String) attributes.get("login"), // 깃허브에 이름이 없으면 login(아이디)을 쓰도록 커스텀 가능
            (String) attributes.get("email"),
            (String) attributes.get("avatar_url") // 깃허브는 picture 대신 avatar_url을 씁니다.
        );
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(
            attributes,
            userNameAttributeName,
            (String) attributes.get("name"),
            (String) attributes.get("email"),
            (String) attributes.get("picture")
        );
    }
}
