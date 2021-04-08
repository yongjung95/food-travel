package com.food.foodtravel.config.auth.dto;

import com.food.foodtravel.domain.user.Role;
import com.food.foodtravel.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String realUserId;
    private String name;
    private String email;
    private String picture;
    private String type;
    private String token;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String realUserId, String name, String email, String picture, String type, String token) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.realUserId = realUserId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.type = type;
        this.token = token;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String token) { // (1)
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes,token);
        }
        if("kakao".equals(registrationId)){
            return ofKakao("id",attributes,token);
        }
        if("facebook".equals(registrationId)){
            return ofFacebook(userNameAttributeName,attributes,token);
        }


        return ofGoogle(userNameAttributeName, attributes,token);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String token) {

        return OAuthAttributes.builder()
                .realUserId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .type("google")
                .token(token)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes, String token) {

        return OAuthAttributes.builder()
                .realUserId((String) attributes.get("id"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .type("facebook")
                .token(token)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, String token) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response"); // (3)

        return OAuthAttributes.builder()
                .realUserId((String) response.get("id"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .type("naver")
                .token(token)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, String token) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .realUserId( attributes.get("id").toString())
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) profile.get("profile_image"))
                .type("kakao")
                .token(token)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .realUserId(realUserId)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .token(token)
                .type(type)
                .build();
    }
}
