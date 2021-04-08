package com.food.foodtravel.domain.post;

import com.food.foodtravel.domain.BaseTimeEntity;
import com.food.foodtravel.domain.image.Image;
import com.food.foodtravel.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private String local;

    @Column(nullable = false)
    private String xpoint;

    @Column(nullable = false)
    private String ypoint;

    @Column(nullable = false)
    private String area;

    @Column
    private int likecnt;

    @Column
    private int hit;

    @OneToMany(cascade = { CascadeType.DETACH,CascadeType.REMOVE },mappedBy = "post")
    private List<Image> imageList;

    @OneToMany(cascade = { CascadeType.DETACH,CascadeType.REMOVE }, mappedBy = "post", fetch = FetchType.EAGER)
    private List<Favorite> favorite;


    @Builder
    public Post(Long postId, String title, String content, User user, String local, String xpoint, String ypoint, String area, int likecnt, int hit, List<Image> imageList){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.user = user;
        this.local = local;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.area = area;
        this.likecnt = likecnt;
        this.hit = hit;
        this.imageList = imageList;
    }

    public PostDto convertEntityToPostDto() {
        return PostDto.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .userDto(user.convertEntityToUserDto())
                .local(local)
                .xpoint(xpoint)
                .ypoint(ypoint)
                .area(area)
                .likecnt(likecnt)
                .hit(hit)
                .build();
    }
}
