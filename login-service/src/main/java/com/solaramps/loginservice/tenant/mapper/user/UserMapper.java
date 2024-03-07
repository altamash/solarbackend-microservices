package com.solaramps.loginservice.tenant.mapper.user;

import com.solaramps.loginservice.tenant.model.user.User;
import com.solaramps.loginservice.tenant.model.user.role.Role;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .acctId(user.getAcctId())
                .jwtToken(user.getJwtToken())
                .compKey(user.getCompKey())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .IdCode(user.getIdCode())
                .authorityId(user.getAuthorityId())
                .gender(user.getGender())
                .dataOfBirth(user.getDataOfBirth())
                .registerDate(user.getRegisterDate())
                .activeDate(user.getActiveDate())
                .status(user.getStatus())
                .notes(user.getNotes())
                .prospectStatus(user.getProspectStatus())
                .referralEmail(user.getReferralEmail())
                .deferredContactDate(user.getDeferredContactDate())
                .language(user.getLanguage())
                .authentication(user.getAuthentication())
                .userType(user.getUserType().getName().getName())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
//                .privileges(user.getPrivileges().stream().map(privilege -> privilege.getName()).collect(Collectors
//                .toSet()))
                .category(user.getCategory())
                .groupId(user.getGroupId())
                .photo(user.getPhoto())
                .socialUrl(user.getSocialUrl())
                .emailAddress(user.getEmailAddress())
                .ccd(user.getCcd())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
