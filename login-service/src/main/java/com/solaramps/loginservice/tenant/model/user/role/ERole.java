package com.solaramps.loginservice.tenant.model.user.role;

import java.util.Arrays;

public enum ERole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER"),
    ROLE_EPC_CUSTOMER("EPC_CUSTOMER"),
    ROLE_NEW_CUSTOMER("ROLE_NEW_CUSTOMER");
    String name;

    ERole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ERole get(String name) {
        return Arrays.stream(values()).filter(value -> name.equalsIgnoreCase(value.name)).findFirst().orElse(null);
    }
}
