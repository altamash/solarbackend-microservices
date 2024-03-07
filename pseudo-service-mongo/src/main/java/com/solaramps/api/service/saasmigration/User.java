package com.solaramps.api.service.saasmigration;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long acctId;
    private String jwtToken;
    private Long compKey;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Date registerDate;
    private Date activeDate;
    private String status;
    private String referralEmail;
    private Date deferredContactDate;
    private String language;
    private String authentication;
    private String userType;
    private String socialUrl;
    private String emailAddress;
    private Boolean ccd;
    private List<String> roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
