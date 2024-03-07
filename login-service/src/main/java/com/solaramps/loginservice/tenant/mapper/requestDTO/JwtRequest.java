package com.solaramps.loginservice.tenant.mapper.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = -5327628407240350793L;
    private String userName;
    private String password;
}
