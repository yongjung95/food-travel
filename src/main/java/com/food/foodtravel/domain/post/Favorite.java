package com.food.foodtravel.domain.post;

import com.food.foodtravel.domain.BaseTimeEntity;
import com.food.foodtravel.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Favorite extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH} , fetch = FetchType.EAGER)
    private Post post;

    @Builder
    public Favorite(Long seq, User user, Post post){
        this.seq = seq;
        this.user = user;
        this.post = post;
    }

    public FavoriteDto converEntityToFavoriteDto(){
        return FavoriteDto.builder()
                .seq(seq)
                .user(user)
                .post(post)
                .build();
    }
}
