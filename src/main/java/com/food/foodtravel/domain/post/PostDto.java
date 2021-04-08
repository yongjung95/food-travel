package com.food.foodtravel.domain.post;

import com.food.foodtravel.domain.image.ImageDto;
import com.food.foodtravel.domain.user.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;

    private String title;

    private String content;

    private UserDto userDto;

    private String local;

    private String xpoint;

    private String ypoint;

    private String area;

    private int likecnt;

    private int hit;

    private List<ImageDto> imageDtoList;

    public Post toEntity(){

        Post post = Post.builder()
                        .postId(postId)
                        .title(title)
                        .content(content)
                        .user(userDto.toEntity())
                        .local(local)
                        .xpoint(xpoint)
                        .ypoint(ypoint)
                        .area(area)
                        .likecnt(likecnt)
                        .hit(hit)
                        .build();
        return post;
    }

    @Builder
    public PostDto(Long postId,String title, String content, UserDto userDto, String local, String xpoint, String ypoint, String area, int likecnt, int hit,List<ImageDto> imageDtoList){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userDto = userDto;
        this.local = local;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.area = area;
        this.likecnt = likecnt;
        this.hit = hit;
        this.imageDtoList = imageDtoList;
    }

}
