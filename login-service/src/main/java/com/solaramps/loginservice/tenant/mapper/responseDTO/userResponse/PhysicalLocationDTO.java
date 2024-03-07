package com.solaramps.loginservice.tenant.mapper.responseDTO.userResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhysicalLocationDTO {

    private Long id;
    private String locationName;
    private String locationType;
    private String otherDetails;
    private String add1;
    private String add2;
    private String add3;
    private String contactPerson;
    private String phone;
    private String category; //ext;
    private String email;
    private String externalRefId;
    private String geoLat;
    private String geoLong;
    private String googleCoordinates;
    private String terrainType;
    private String status;
    private String active;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private Boolean correspondenceAddress;



}
