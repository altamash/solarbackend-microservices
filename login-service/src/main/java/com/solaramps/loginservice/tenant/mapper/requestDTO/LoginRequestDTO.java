package com.solaramps.loginservice.tenant.mapper.requestDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequestDTO implements Serializable {
    private String userCredential;

    private String password;

    private Long subDepartmentId;
}
