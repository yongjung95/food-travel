package com.food.foodtravel.web;

import com.food.foodtravel.domain.Service.PostService;
import com.food.foodtravel.domain.Service.UserService;
import com.food.foodtravel.domain.post.PostDto;
import com.food.foodtravel.domain.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final HttpSession httpSession;
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/post/post.do")
    private Map<String,Object> posting (PostDto postDto, List<MultipartFile> fileList ) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = getSession();

        if (userDto != null) {
            postDto.setUserDto(userDto);

            postService.save(postDto, fileList);

            resultMap.put("result", "000");

            return resultMap;
        }

        resultMap.put("result","043");

        return resultMap;
    }

    @PutMapping("/post/update.do")
    private Map<String,Object> updating (PostDto postDto, List<MultipartFile> fileList,@RequestParam(required = false) Long [] deleteImage ) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        ArrayList<Long> deleteImageList = new ArrayList<>();

        if ( deleteImage != null ){
            deleteImageList = new ArrayList<>(Arrays.asList(deleteImage));
        }

        UserDto userDto = getSession();

        if (userDto != null) {
            postDto.setUserDto(userDto);

            postService.update(postDto, fileList, deleteImageList);

            resultMap.put("result","000");

            return resultMap;
        }

        resultMap.put("result","043");

        return resultMap;


    }

    @DeleteMapping("/post/delete.do")
    private void delete(Long id) throws IOException {
        postService.delete(id);
    }

    @GetMapping("/post/searchList")
    private Map<String,Object> searchList(@PageableDefault(size = 2) Pageable pageable , @RequestParam(required = false) String searchKeyword){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<PostDto> postDtoList = postService.getSeachList(searchKeyword, pageable);
        resultMap.put("postDtoList", postDtoList);

        return resultMap;
    }

    @PutMapping("/post/Favorite.do")
    private Map<String, Object> Favorite(Long id){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = getSession();

        PostDto postDto = postService.detail(id, false);

        boolean result = postService.favorite(userDto, postDto);

        if ( result ){
            resultMap.put("result","000");

            return resultMap;
        }

        resultMap.put("result","200");

        return resultMap;
    }

    @GetMapping("/post/paging")
    private Map<String, Object> paging(@PageableDefault(size = 2) Pageable pageable , @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchKeyword){

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<PostDto> postDtoList = postService.getSeachList(searchKeyword,pageable);

        resultMap.put("postDtoList",postDtoList);
        resultMap.put("isLast", postService.lastPagingCheck(searchKeyword,pageable));
        resultMap.put("totalPage",postService.totalPage(searchKeyword,pageable));

        return resultMap;

    }

    private UserDto getSession(){
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");

        UserDto user = null ;

        if (userDto != null){
            user = userService.getSessionUser(userDto);
        }

        return user;
    }

}
