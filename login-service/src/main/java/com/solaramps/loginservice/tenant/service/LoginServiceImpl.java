package com.solaramps.loginservice.tenant.service;

import com.solaramps.loginservice.configuration.UserTenantInformation;
import com.solaramps.loginservice.configuration.jwt.JwtTokenUtil;
import com.solaramps.loginservice.saas.configuration.DBContextHolder;
import com.solaramps.loginservice.saas.model.tenant.MasterTenant;
import com.solaramps.loginservice.saas.service.MasterTenantService;
import com.solaramps.loginservice.tenant.mapper.requestDTO.JwtRequest;
import com.solaramps.loginservice.tenant.mapper.user.UserDTO;
import com.solaramps.loginservice.tenant.mapper.user.UserMapper;
import com.solaramps.loginservice.tenant.model.user.User;
import com.solaramps.loginservice.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
//@Transactional("transactionManager")
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//    @Autowired
    private final MasterTenantService masterTenantService;
    private final UserService userService;
//    @Autowired
//    @Qualifier("tenantAuthenticationManager")
    private final AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsService jwtUserDetailsService;
//    @Autowired
    private final UserDetailsService jwtUserDetailsService;
//    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    private Map<String, String> mapValue = new HashMap<>();
    private Map<String, String> userDbMap = new HashMap<>();

//    @Autowired
//    private AdminInterface adminInterface;
//    @Autowired
//    private SolarAmpsInterface solarAmpsInterface;

    public LoginServiceImpl(MasterTenantService masterTenantService,
                            UserService userService, @Qualifier("tenantAuthenticationManager") AuthenticationManager authenticationManager,
                            @Qualifier("userDetailsService") UserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.masterTenantService = masterTenantService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
//        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDTO login(JwtRequest requestDTO, Long compKey, HttpServletRequest request) throws Exception {

        LOGGER.info("LOGIN PROCESS STARTED ::::");

        long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        Authentication authentication = authenticate(requestDTO.getUserName(), requestDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByUserNameFetchRoles(requestDTO.getUserName());
//        solarAmpsInterface.setTenant(masterTenant.getDbName());
        String userName = requestDTO.getUserName();
        MasterTenant masterTenant = masterTenantService.findById(compKey);
        loadCurrentDatabaseInstance(masterTenant, requestDTO.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        String jwtToken = jwtTokenUtil.generateToken(userDetails, String.valueOf(masterTenant.getId()), masterTenant.getTenantTier());

        /*UserDTO userDTO = solarAmpsInterface.fetchUserByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Username:" + userName+ " not found"));*/
//        UserDTO userDTO = ((UserDetailsServiceImpl) jwtUserDetailsService).getUser();
        user.setJwtToken(jwtToken);
        setMetaDataAfterLogin();
        MDC.put("Comp-Key", String.valueOf(compKey));
        LOGGER.info("LOGIN PROCESS COMPLETED IN ::: " + (DateUtils.getTimeInMillisecondsFromLocalDate() - startTime)
                + " ms");
        LOGGER.info("Logged in as " + userName);
//        solarAmpsInterface.setTenant(masterTenant.getDbName());
        return UserMapper.toUserDTO(user);
//        return jwtToken;
    }

    private Authentication authenticate(String username, String password) throws Exception {

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            return null;
        } catch (DisabledException e) {
            LOGGER.error("User disabled", e);
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            LOGGER.error("Invalid UserName or Password", e);
            throw new BadCredentialsException("Invalid UserName or Password", e);
        }
    }

    private void loadCurrentDatabaseInstance(MasterTenant masterTenant, String userName) {
        DBContextHolder.setTenantName(masterTenant.getDbName());
        mapValue.put(userName, masterTenant.getDbName());
    }

    @Bean(name = "userTenantInfo")
    @ApplicationScope
    public UserTenantInformation setMetaDataAfterLogin() {
        UserTenantInformation tenantInformation = new UserTenantInformation();
        if (mapValue.size() > 0) {
            for (String key : mapValue.keySet()) {
                if (null == userDbMap.get(key)) {
                    userDbMap.putAll(mapValue);
                } else {
                    userDbMap.put(key, mapValue.get(key));
                }
            }
            mapValue = new HashMap<>();
        }
        tenantInformation.setMap(userDbMap);
        return tenantInformation;
    }
    /*private Function<JwtRequest, UserDTO> fetchAdminDetails = (loginRequestDTO) -> {

        Pattern pattern = Pattern.compile(EmailConstants.EMAIL_PATTERN);
        Matcher m = pattern.matcher(loginRequestDTO.getUserCredential());

        return m.find() ? solarAmpsInterface.searchAdmin
                (JwtRequest.builder().username(null).emailAddress(loginRequestDTO.getUserCredential()).build())
                : solarAmpsInterface.searchAdmin
                (AdminRequestDTO.builder().username(loginRequestDTO.getUserCredential()).emailAddress(null).build());
    };

    private Consumer<UserDTO> validateAdminUsername = (admin) -> {
        if (Objects.isNull(admin))
            throw new UnauthorisedException(InvalidAdminUsername.MESSAGE, InvalidAdminUsername.DEVELOPER_MESSAGE);
        LOGGER.info(":::: ADMIN USERNAME VALIDATED ::::");
    };

    private Consumer<UserDTO> validateAdminStatus = (admin) -> {

        switch (admin.getStatus()) {
            case 'B':
                throw new UnauthorisedException(InvalidAdminStatus.MESSAGE_FOR_BLOCKED,
                        InvalidAdminStatus.DEVELOPER_MESSAGE_FOR_BLOCKED);

            case 'N':
                throw new UnauthorisedException(InvalidAdminStatus.MESSAGE_FOR_INACTIVE,
                        InvalidAdminStatus.DEVELOPER_MESSAGE_FOR_INACTIVE);
        }
        LOGGER.info(":::: ADMIN STATUS VALIDATED ::::");
    };

    private BiConsumer<JwtRequest, UserDTO> validatePassword = (requestDTO, admin) -> {

        LOGGER.info(":::: ADMIN PASSWORD VALIDATION ::::");

        if (BCrypt.checkpw(requestDTO.getPassword(), admin.getPassword())) {
            admin.setLoginAttempt(0);
            solarAmpsInterface.updateAdmin(admin);
        } else {
            admin.setLoginAttempt(admin.getLoginAttempt() + 1);

            if (admin.getLoginAttempt() >= 3) {
                admin.setStatus('B');
                solarAmpsInterface.updateAdmin(admin);

                LOGGER.debug("ADMIN IS BLOCKED DUE TO MULTIPLE WRONG ATTEMPTS...");
                throw new UnauthorisedException(IncorrectPasswordAttempts.MESSAGE,
                        IncorrectPasswordAttempts.DEVELOPER_MESSAGE);
            }

            LOGGER.debug("INCORRECT PASSWORD...");
            throw new UnauthorisedException(ForgetPassword.MESSAGE, ForgetPassword.DEVELOPER_MESSAGE);
        }

        LOGGER.info(":::: ADMIN PASSWORD VALIDATED ::::");
    };*/

}

