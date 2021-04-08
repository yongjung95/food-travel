package com.food.foodtravel.domain.user;


import com.food.foodtravel.domain.BaseTimeEntity;
import com.food.foodtravel.domain.post.Favorite;
import com.food.foodtravel.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String realUserId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String type;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String phoneNumber;

    @Column
    private String token;

    @Column(nullable = false)
    private String xpoint;

    @Column(nullable = false)
    private String ypoint;

    @Column(nullable = false)
    private String area;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE}, mappedBy = "user", fetch=FetchType.LAZY)
    private List<Post> postList;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE}, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Favorite> favoriteList;

    @Builder
    public User(Long userId,String realUserId, String name, String email, String picture, Role role, String type, String nickname, String phoneNumber, String token, String xpoint, String ypoint, String area) {
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

    public User update(String realUserId, String name, String picture, String token) {
        this.realUserId = realUserId;
        this.name = name;
        this.picture = picture;
        this.token = token;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public UserDto convertEntityToUserDto(){
        return UserDto.builder()
                .userId(userId)
                .realUserId(realUserId)
                .name(name)
                .email(email)
                .picture(picture)
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
