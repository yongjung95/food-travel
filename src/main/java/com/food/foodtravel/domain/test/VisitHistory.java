package com.food.foodtravel.domain.test;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
public class VisitHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private BigInteger visitID;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalId",nullable = false)
    private Hospital hospital;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    // 접수일시 추가 필요.

    @Column(nullable = false, length = 10)
    private String visitStateCode;
}
