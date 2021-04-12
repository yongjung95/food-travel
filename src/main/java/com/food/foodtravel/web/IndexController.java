package com.food.foodtravel.web;

import com.food.foodtravel.domain.Service.PostService;
import com.food.foodtravel.domain.Service.UserService;
import com.food.foodtravel.domain.post.PostDto;
import com.food.foodtravel.domain.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final HttpSession httpSession;
    private final PostService postService;
    private final UserService userService;

    private UserDto userDto;

    // 메인페이지 요청
    @GetMapping("/")
    public String index(Model model,@PageableDefault(sort = {"hit"},direction = Sort.Direction.DESC, size = 2) Pageable pageable){

        getSession(model);

        List<PostDto> postDtoList = postService.getPagingList(pageable);
        model.addAttribute(postDtoList);

        return "index";
    }

    // 로그인 페이지 요청
    @GetMapping("/loginPage")
    public String loginPage(Model model){
        return "loginPage";
    }

    // sns 로그인 성공 요청
    @GetMapping("/loginResult")
    public String loginResult (){
        userDto = (UserDto) httpSession.getAttribute("userDto");

        if ( userDto != null ){
            if ( !userService.getUserByEmailAndType(userDto) ){
                return "signUp/newSignUp";
            }
        }

        return "redirect:/";
    }

    // 가입 후 정보 입력 후 거주지 정보 요청
    @GetMapping("/signUp/settings")
    private String settings (Model model){
        userDto = (UserDto) httpSession.getAttribute("userDto");

        if (userDto != null){
            if ( !userService.getUserByEmailAndType(userDto) ){
                return "signUp/settings";
            }
        }

        return "redirect:/";
    }

    // 마이페이지 요청
    @GetMapping("/myPage")
    private String myPage (Model model){
        getSession(model);

        return "myPage";
    }

    // 즐겨찾기 페이지 요청
    @GetMapping("/myFavoriteList")
    private String myFavoriteList (Model model){
        getSession(model);

        //UserDto userDto = userService.getSessionUser((UserDto) httpSession.getAttribute("userDto"));
        List<PostDto> postDtoList = postService.getFavoriteList((UserDto) httpSession.getAttribute("userDto"));

        if ( postDtoList.size() > 0 ){
            model.addAttribute("postDtoList",postDtoList);
        }

        return "post/myFavoriteList";
    }

    // postList 페이지 요청
    @GetMapping("/postList")
    private String postList (Model model,@PageableDefault(size = 2) Pageable pageable){
        getSession(model);

        List<PostDto> postDtoList = postService.getPagingList(pageable);
        model.addAttribute(postDtoList);

        return "post/postList";
    }

    // 글 작성 페이지 요청
    @GetMapping("/postAdd")
    private String postAdd (Model model){
        getSession(model);

        return "post/postAdd";
    }

    @GetMapping("/post/detail/{postId}")
    private String getPost(Model model, @PathVariable Long postId){
        getSession(model);

        PostDto postDto = postService.detail(postId,false);

        if ( postDto != null ){
            UserDto userDto = userService.getSessionUser((UserDto) httpSession.getAttribute("userDto"));

            boolean favoriteCheck = postService.favoriteCheck(userDto,postDto);

            model.addAttribute("favoriteCheck",favoriteCheck);
            model.addAttribute("postDto",postDto);
        }

        return "post/postDetail";
    }

    @GetMapping("/post/update/{postId}")
    private String getUpdate(Model model, @PathVariable Long postId){
        getSession(model);

        PostDto postDto = postService.detail(postId,true);

        if ( postDto != null ){
            model.addAttribute("postDto",postDto);
        }

        return "post/postUpdate";
    }

    // 세션에 있는 유저 정보 가져오기.
    private void getSession(Model model){
        userDto = (UserDto) httpSession.getAttribute("userDto");
        System.out.println("test1");
        if (userDto != null){
            System.out.println("test2");
           model.addAttribute("sessionUser",userService.getSessionUser(userDto));
        }

    }



}
