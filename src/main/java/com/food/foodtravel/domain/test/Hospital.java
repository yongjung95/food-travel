package com.food.foodtravel.domain.test;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Getter
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private BigInteger hospitalID;

    @Column(nullable = false,length = 45)
    private String hospitalName;

    @Column(nullable = false,length = 20)
    private String healthcareFacilityId;

    @Column(nullable = false,length = 10)
    private String hospitalDirector;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE}, mappedBy = "hospital", fetch=FetchType.LAZY)
    private List<VisitHistory> visitHistoryList;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE}, mappedBy = "hospital", fetch=FetchType.LAZY)
    private List<Patient> patientList;

}
