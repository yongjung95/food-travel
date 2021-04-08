package com.food.foodtravel.domain.image;

import com.food.foodtravel.domain.post.Post;
import com.food.foodtravel.domain.post.PostDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String imageName;
    private String imagePath;
    private String realImageName;
    private PostDto postDto;


    public Image toEntity(Post post){

        Image image = Image.builder()
                            .imageName(imageName)
                            .imagePath(imagePath)
                            .realImageName(realImageName)
                            .post(post)
                            .build();
        return image;
    }


    @Builder
    public ImageDto(Long id, String imageName, String imagePath,String realImageName, PostDto postDto){
        this.id = id;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.realImageName = realImageName;
        this.postDto = postDto;

    }
}
