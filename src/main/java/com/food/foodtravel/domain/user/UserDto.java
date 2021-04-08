package com.food.foodtravel.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = -4913613412810509084L;

    private Long userId;

    private String realUserId;

    private String name;

    private String email;

    private String picture;

    private Role role;

    private String type;

    private String nickname;

    private String phoneNumber;

    private String token;

    private String xpoint;

    private String ypoint;

    private String area;

    @Builder
    public UserDto(Long userId,String realUserId, String name, String email, String picture, Role role, String type, String nickname, String phoneNumber, String token, String xpoint, String ypoint, String area){
        this.userId = userId;
        this.realUserId = realUserId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.type = type;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.token = token;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.area = area;
    }

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .realUserId(realUserId)
                .name(name)
                .email(email)
                .picture(picture)
                .role(role)
                .type(type)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .token(token)
                .xpoint(xpoint)
                .ypoint(ypoint)
                .area(area)
                .build();
    }
}
