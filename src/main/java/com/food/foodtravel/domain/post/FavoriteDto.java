package com.food.foodtravel.domain.post;


import com.food.foodtravel.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteDto {

    private Long seq;

    private User user;

    private Post post;

    public Favorite toEntity(){
        Favorite favorite = Favorite.builder()
                                    .seq(seq)
                                    .user(user)
                                    .post(post)
                                    .build();

        return favorite;
    }

    @Builder
    public FavoriteDto(Long seq, User user, Post post){
        this.seq = seq;
        this.user = user;
        this.post = post;
    }
}
