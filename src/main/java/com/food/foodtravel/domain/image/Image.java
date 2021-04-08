package com.food.foodtravel.domain.image;

import com.food.foodtravel.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageName;

    @Column
    private String imagePath;

    @Column
    private String realImageName;

    @ManyToOne(cascade = CascadeType.DETACH,fetch= FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Builder
    public Image(String imageName, String imagePath,String realImageName,Post post){
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.realImageName = realImageName;
        this.post = post;

    }

    public ImageDto convertEntityToImageDto() {
        return ImageDto.builder()
                .id(id)
                .imageName(imageName)
                .imagePath(imagePath)
                .realImageName(realImageName)
                .build();
    }
}
