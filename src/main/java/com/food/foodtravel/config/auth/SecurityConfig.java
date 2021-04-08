package com.food.foodtravel.config.auth;


import com.food.foodtravel.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "com.food.foodtravel") // JpaRepository 패키지 위치 등록
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()// (2)
                .and()
                .authorizeRequests()// (3)
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**","/loginPage","/signUp/**","/loginResult","/assets/**","/gallery/**","/font/**").permitAll()
                .antMatchers("/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginPage")
                .defaultSuccessUrl("/loginResult")
                .successHandler(new LoginSuccessHandler())
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
