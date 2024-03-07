package com.solaramps.loginservice.tenant.model.user.userType;

import java.util.Arrays;

public enum EUserType {
    HO("HO"),
    CUSTOMER("CUSTOMER"),
    COMMERCIAL("COMMERCIAL"),
    RESIDENTIAL("RESIDENTIAL");


    String name;

    EUserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EUserType get(String name) {
        return Arrays.stream(values()).filter(value -> name.equalsIgnoreCase(value.name)).findFirst().orElse(null);
    }
}
