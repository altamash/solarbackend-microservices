package com.solaramps.loginservice.tenant.service;

import com.solaramps.loginservice.tenant.mapper.requestDTO.JwtRequest;
import com.solaramps.loginservice.tenant.mapper.user.UserDTO;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

     UserDTO login(JwtRequest requestDTO, Long compKey, HttpServletRequest request) throws Exception;

}
