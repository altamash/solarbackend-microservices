package com.solaramps.loginservice.tenant.service.userDetails;

import com.solaramps.loginservice.saas.service.MasterTenantService;
import com.solaramps.loginservice.tenant.mapper.responseDTO.userResponse.UserDTO;
import com.solaramps.loginservice.tenant.model.user.User;
import com.solaramps.loginservice.tenant.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MasterTenantService masterTenantService;
    private final UserRepository userRepository;
    private ThreadLocal<UserDTO> user = new ThreadLocal<>();

    public UserDetailsServiceImpl(MasterTenantService masterTenantService,
                                  UserRepository userRepository) {
        this.masterTenantService = masterTenantService;
        this.userRepository = userRepository;
    }

    public UserDTO getUser() {
        return this.user.get();
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameFetchRoles(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return UserDetailsImpl.build(user);
    }
}
