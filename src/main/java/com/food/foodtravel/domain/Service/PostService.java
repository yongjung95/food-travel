package com.food.foodtravel.domain.Service;


import com.food.foodtravel.domain.image.Image;
import com.food.foodtravel.domain.image.ImageDto;
import com.food.foodtravel.domain.image.ImageRepository;
import com.food.foodtravel.domain.post.*;
import com.food.foodtravel.domain.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;
    private final S3Service s3Service;

    @Transactional
    public List<PostDto> getList() {
        List<Post> postList = postRepository.findAll(Sort.by("createdDate").descending());

        List<PostDto> postDtoList = new ArrayList<>();

        List<Image> imageList;


        for ( Post post : postList ) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            imageList = imageRepository.findAllByPost(post);

            for (Image image : imageList){
                imageDtoList.add(image.convertEntityToImageDto());
            }

            PostDto postDto = post.convertEntityToPostDto();
            postDto.setImageDtoList(imageDtoList);
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Transactional
    public List<PostDto> getPagingList(Pageable pageable) {
        Page<Post> postList = postRepository.findAll(pageable);

        List<PostDto> postDtoList = new ArrayList<>();

        List<Image> imageList;


        for ( Post post : postList ) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            imageList = imageRepository.findAllByPost(post);

            for (Image image : imageList){
                imageDtoList.add(image.convertEntityToImageDto());
            }

            PostDto postDto = post.convertEntityToPostDto();
            postDto.setImageDtoList(imageDtoList);
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Transactional
    public boolean lastPagingCheck(String searchKeyword, Pageable pageable){
        Page<Post> postList;

        if ( searchKeyword != null ){
            postList = postRepository.findByContentContainingOrTitleContaining(searchKeyword, searchKeyword, pageable);
        }else{
            postList = postRepository.findAll(pageable);
        }

        if ( postList.isLast() ){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public int totalPage(String searchKeyword, Pageable pageable){
        Page<Post> postList;

        if ( searchKeyword != null ){
            postList = postRepository.findByContentContainingOrTitleContaining(searchKeyword, searchKeyword, pageable);
        }else{
            postList = postRepository.findAll(pageable);
        }

        return postList.getTotalPages();
    }

    @Transactional
    public List<PostDto> getSeachList(String searchKeyword, Pageable pageable){
        Page<Post> postList;

        if ( searchKeyword != null ){
            postList = postRepository.findByContentContainingOrTitleContaining(searchKeyword, searchKeyword, pageable);
        }else{
            postList = postRepository.findAll(pageable);
        }

        List<PostDto> postDtoList = new ArrayList<>();

        List<Image> imageList;


        for ( Post post : postList ) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            imageList = imageRepository.findAllByPost(post);

            for (Image image : imageList){
                imageDtoList.add(image.convertEntityToImageDto());
            }

            PostDto postDto = post.convertEntityToPostDto();
            postDto.setImageDtoList(imageDtoList);
            postDtoList.add(postDto);
        }

        return postDtoList;

    }

    @Transactional
    public PostDto detail(Long postId,boolean update){

       Post post = postRepository.findById(postId).orElse(null);

       PostDto postDto = null;
       List<Image> imageList;
       List<ImageDto> imageDtoList = new ArrayList<>();

       if ( post != null ){
           postDto = post.convertEntityToPostDto();

           if ( !update ){
               postDto.setHit(postDto.getHit() + 1);

               postRepository.save(postDto.toEntity());
           }

           imageList = imageRepository.findAllByPost(post);

           for (Image image : imageList){
               imageDtoList.add(image.convertEntityToImageDto());
           }

           postDto.setImageDtoList(imageDtoList);


       }

       return postDto;
    }

    @Transactional
    public void save(PostDto postDto,List<MultipartFile> fileList) throws IOException {

        List<MultipartFile> files = new ArrayList<>();
        List<ImageDto> imageDtoList;

        if ( fileList != null ){
            for ( MultipartFile file : fileList ){
                if ( file.getSize() > 0 ){
                    files.add(file);
                }
            }
        }

        imageDtoList = s3Service.upload(files);
        Post post = postDto.toEntity();
        postRepository.save(post);

        for ( ImageDto imageDto : imageDtoList ){
            imageRepository.save(imageDto.toEntity(post));
        }

    }
    @Transactional
    public void delete(Long id) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = "+ id));

        List<Image> imageList = imageRepository.findAllByPost(post);

        List<ImageDto> imageDtoList = new ArrayList<>();

        for (Image image : imageList){
            s3Service.delete(image.getImageName());
        }

        postRepository.delete(post);
    }

    @Transactional
    public void update(PostDto postDto, List<MultipartFile> fileList, List<Long> deleteImage) throws IOException{

        if ( deleteImage.size() > 0 ){
            List<Image> imageList = new ArrayList<>();

            for ( Long imageId : deleteImage ){
                imageList.add(imageRepository.findById(imageId).orElse(null));
            }

            for ( Image image : imageList ){
                imageRepository.delete(image);
                s3Service.delete(image.getImageName());
            }
        }

        List<MultipartFile> files = new ArrayList<>();
        List<ImageDto> imageDtoList;

        if ( fileList != null ){
            for ( MultipartFile file : fileList ){
                if ( file.getSize() > 0 ){
                    files.add(file);
                }
            }
        }

        imageDtoList = s3Service.upload(files);
        Post post = postDto.toEntity();
        postRepository.save(post);

        for ( ImageDto imageDto : imageDtoList ){
            imageRepository.save(imageDto.toEntity(post));
        }

    }

    @Transactional
    public boolean favorite(UserDto userDto, PostDto postDto){

        Optional<Favorite> favorite = favoriteRepository.findByUserAndPost(userDto.toEntity(), postDto.toEntity());

        if (!favorite.isPresent()){
            FavoriteDto favoriteDto = FavoriteDto.builder()
                    .user(userDto.toEntity())
                    .post(postDto.toEntity())
                    .build();

            favoriteRepository.save(favoriteDto.toEntity());
            postDto.setLikecnt(postDto.getLikecnt()+1);
            postRepository.save(postDto.toEntity());

            return true;
        }

        favoriteRepository.delete(favorite.get());
        postDto.setLikecnt(postDto.getLikecnt()-1);
        postRepository.save(postDto.toEntity());

        return false;

    }

    @Transactional
    public boolean favoriteCheck(UserDto userDto, PostDto postDto){

        Optional<Favorite> favorite = favoriteRepository.findByUserAndPost(userDto.toEntity(), postDto.toEntity());

        boolean favoriteCheck = false;
        if (favorite.isPresent()){
            favoriteCheck = true;
        }

        return favoriteCheck;
    }

    @Transactional
    public List<PostDto> getFavoriteList(UserDto userDto){
        List<Favorite> favoriteList = favoriteRepository.findAllByUser(userDto.toEntity());

        List<PostDto> postDtoList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();

        if ( favoriteList.size() > 0 ){
            for ( Favorite favorite : favoriteList ){
                Optional<Post> post = postRepository.findById(favorite.getPost().getPostId());
                postList.add(post.get());
            }

            List<Image> imageList;

            for ( Post post : postList ) {
                List<ImageDto> imageDtoList = new ArrayList<>();
                imageList = imageRepository.findAllByPost(post);

                for (Image image : imageList){
                    imageDtoList.add(image.convertEntityToImageDto());
                }

                PostDto postDto = post.convertEntityToPostDto();
                postDto.setImageDtoList(imageDtoList);
                postDtoList.add(postDto);
            }
        }

        return postDtoList;

    }
}
