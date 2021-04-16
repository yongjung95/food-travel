package com.food.foodtravel.domain.test;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Getter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private BigInteger patientID;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalId", nullable = false)
    private Hospital hospital;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 13)
    private String gender;

    @Column(length = 10)
    private String birth;

    @Column(length = 20)
    private String phoneNumber;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE}, mappedBy = "patient", fetch=FetchType.LAZY)
    private List<VisitHistory> visitHistoryList;

}
