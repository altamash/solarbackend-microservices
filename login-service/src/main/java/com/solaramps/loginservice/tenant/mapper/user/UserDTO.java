package com.solaramps.loginservice.tenant.mapper.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.solaramps.loginservice.tenant.mapper.responseDTO.userResponse.PhysicalLocationDTO;
import com.solaramps.loginservice.tenant.mapper.user.address.AddressDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    private Long acctId;
    private String jwtToken;
    private Long compKey;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String IdCode;       //Authority ID
    private String authorityId;  //e.g. SSN, Driving License
    private String gender;
    private Date dataOfBirth;
    private Date registerDate;
    private Date activeDate;
    private String status;
    private String notes;
    private String prospectStatus;
    private String referralEmail;
    private Date deferredContactDate;
    private String language;
    private String authentication;
    private String userType;
    private Set<String> roles;
    //    private Set<String> privileges;
    private String category;
    private String groupId;
    private byte[] photo;
    private String photoBase64;
    private String socialUrl;
    private String emailAddress;
    private Boolean ccd;
    private String businessLogoBase64;
    private String businessCompanyName;
    private String businessWebsite;
    private String businessPhone;
    private String businessEmail;
//    private List<CustomerSubscriptionDTO> customerSubscriptions;
    private Set<AddressDTO> addresses;
    private List<PhysicalLocationDTO> physicalLocations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isAttachment;
    private String customerType;

}
