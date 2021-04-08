package com.food.foodtravel.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Api {
    @Value("${kakaomap.api}")
    private String kakaoMapApi;
}
