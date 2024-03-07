package com.solaramps.api.utils;

import java.util.Arrays;

public enum EProductParsers {

    CSG_PRE_P("CSG_POST_R_R","CSG_POST_R_R"),
    CSG_PRE_R_R("CSG_R_PRE","CSG_R_PRE");

    String code;
    String path;

    public String getCode(){
        return code;
    }

    public String getPath() {
        return path;
    }

    EProductParsers(String code, String path) {
        this.code = code;
        this.path = path;
    }

    public static EProductParsers get(String code) {
       return Arrays.stream(values()).filter(value -> code.equalsIgnoreCase(value.code)).findFirst().orElse(null);
    }

}
