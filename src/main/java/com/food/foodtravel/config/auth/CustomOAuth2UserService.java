package com.food.foodtravel.config.auth;

import com.food.foodtravel.config.auth.dto.OAuthAttributes;
import com.food.foodtravel.domain.user.User;
import com.food.foodtravel.domain.user.UserDto;
import com.food.foodtravel.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();


        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes(), userRequest.getAccessToken().getTokenValue());

        User user = saveOrUpdate(attributes,userRequest.getAccessToken().getTokenValue());

        UserDto userDto = user.convertEntityToUserDto();

        httpSession.setAttribute("userDto",userDto );

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private User saveOrUpdate(OAuthAttributes attributes,String token) {

       User user = userRepository.findByEmailAndType(attributes.getEmail(), attributes.getType())
                   .map(entity -> entity.update(attributes.getRealUserId(), attributes.getName(), attributes.getPicture(), token))
                   .orElse(attributes.toEntity());

       if (user.getNickname() == null ){
           return user;
       }

       userRepository.save(user);

       return user;

    }

}
