package com.food.foodtravel.domain.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.foodtravel.config.auth.seed.Seed;
import com.food.foodtravel.domain.user.Role;
import com.food.foodtravel.domain.user.User;
import com.food.foodtravel.domain.user.UserDto;
import com.food.foodtravel.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public boolean getUserByEmailAndType(UserDto userDto){
        User user = userRepository.findByEmailAndType(userDto.getEmail(),userDto.getType()).orElse(null);

        if ( user != null ){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean getUserByNickname(String nickname){
        User user = userRepository.findByNickname(nickname).orElse(null);

        if ( user != null ){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean getUserByPhoneNumber(String phoneNumber){

        String encodePhoneNumber = Seed.encrypt(phoneNumber);

        User user = userRepository.findByPhoneNumber(encodePhoneNumber).orElse(null);

        if ( user != null ){
            return true;
        }
        return false;
    }

    @Transactional
    public UserDto getSessionUser(UserDto userDto){
        User user = userRepository.findByEmailAndType(userDto.getEmail(), userDto.getType()).orElse(null);

        UserDto sessionUser = null;

        if ( user != null ){
            sessionUser = user.convertEntityToUserDto();
        }

        return sessionUser;
    }

    @Transactional
    public void saveUser(UserDto userDto){
        String encodePhoneNumber = Seed.encrypt(userDto.getPhoneNumber());
        userDto.setPhoneNumber(encodePhoneNumber);
        userDto.setRole(Role.USER);
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void deleteUser(UserDto userDto, String type){
        userRepository.deleteByUserId(userDto.getUserId());
        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";

        try {

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = null;
            UriComponents uri = null;
            ResponseEntity<Map> resultMap = null;

            if ("kakao".equals(type)) {
                url = "https://kapi.kakao.com/v1/user/unlink";
                header.add("Authorization", "Bearer " + userDto.getToken());

                uri = UriComponentsBuilder.fromHttpUrl(url).build();
                resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            }else if ("naver".equals(type)){
                url = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=3MrFVPmZEPACLZVLj946&client_secret=8yuMcmEA1X&access_token="+userDto.getToken()+"&service_provider=NAVER";
                uri = UriComponentsBuilder.fromHttpUrl(url).build();
                resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            }else if ("facebook".equals(type)){
                url = "https://graph.facebook.com/"+userDto.getRealUserId()+"/permissions?access_token="+userDto.getToken();
                uri = UriComponentsBuilder.fromHttpUrl(url).build();
                resultMap = restTemplate.exchange(uri.toString(), HttpMethod.DELETE, entity, Map.class);

            }else if ("google".equals(type)){
                header.add("Content-type","application/x-www-form-urlencoded");
                url = "https://oauth2.googleapis.com/revoke?token="+userDto.getToken();
                uri = UriComponentsBuilder.fromHttpUrl(url).build();
                resultMap = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, Map.class);

            }

            //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.

            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());

        }

        System.out.println(jsonInString);
    }
}
