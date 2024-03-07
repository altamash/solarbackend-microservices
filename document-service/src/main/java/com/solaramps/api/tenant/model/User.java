package com.solaramps.api.tenant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctId;
    @Transient
    private String jwtToken;
    private Long compKey;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    private String IdCode;       //Authority ID
    private String authorityId;  //e.g. SSN, Driving License
    private String gender;
    private Date dataOfBirth;
    private Date registerDate;
    private Date activeDate;
    private String status;
    @Column(length = 1000)
    private String notes;
    private String prospectStatus;
    private String referralEmail;
    private Date deferredContactDate;
    private String language;
    private String authentication;
    private boolean isEmailVerified;


    private String category;
    private String groupId;
    @Lob
    private byte[] photo;
    private String socialUrl;
    private String emailAddress;
    private Boolean ccd;


}
