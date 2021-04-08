package com.food.foodtravel.web;

import com.food.foodtravel.domain.Service.UserService;
import com.food.foodtravel.domain.user.UserDto;
import com.food.foodtravel.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SignUpController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final UserService userService;

    @GetMapping("/signUp/nickNameCheck.do")
    private Map<String, Object> nickNameCheck(String nickName){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if ( userService.getUserByNickname(nickName) ){
            resultMap.put("result","100");

            return resultMap;
        }

        resultMap.put("result","000");

        return resultMap;
    }

    @GetMapping("/signUp/phoneNumberCheck.do")
    private Map<String, Object> phoneNumberCheck(String phoneNumber){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if ( userService.getUserByPhoneNumber(phoneNumber) ){
            resultMap.put("result","100");

            return resultMap;
        }

        resultMap.put("result","000");

        return resultMap;
    }

    @PostMapping("/signUp/signUp.do")
    private Map<String, Object> signUp(String nickname,String phoneNumber){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");

        if ( userService.getUserByEmailAndType(userDto) ){
            resultMap.put("result","100");

            return resultMap;
        }

        userDto.setNickname(nickname);
        userDto.setPhoneNumber(phoneNumber);

        httpSession.setAttribute("userDto",userDto);

        resultMap.put("result","000");

        return resultMap;
    }

    @PostMapping("/signUp/settings.do")
    private Map<String, Object> settings(String xpoint, String ypoint, String area){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");

        if ( userService.getUserByEmailAndType(userDto) ){
            resultMap.put("result","100");

            return resultMap;
        }

        userDto.setXpoint(xpoint);
        userDto.setYpoint(ypoint);
        userDto.setArea(area);

        userService.saveUser(userDto);

        resultMap.put("result","000");

        return resultMap;

    }

    @PutMapping("/user/update")
    private Map<String, Object> update(String nickname, String xpoint, String ypoint, String area){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");

        if ( userDto != null ){
            userDto = userService.getSessionUser(userDto);

            userDto.setNickname(nickname);
            userDto.setXpoint(xpoint);
            userDto.setYpoint(ypoint);
            userDto.setArea(area);

            userService.saveUser(userDto);

            resultMap.put("result","000");

            return resultMap;
        }
        resultMap.put("result","010");

        return resultMap;
    }

    @DeleteMapping("/signUp/delete")
    private Map<String, Object> delete(String type){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");

        if ( userDto != null ){
            userDto = userService.getSessionUser(userDto);

            userService.deleteUser(userDto, type);

            resultMap.put("result","000");

            return resultMap;
        }

        resultMap.put("result","010");

        return resultMap;
    }

}
