package com.solaramps.api.service.saasmigration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MigrationAPIBody {

    private String compKey;
    private String userName;
    private String password;
    private String passCode;

    @Override
    public String toString() {
        return "MigrationAPIBody{" +
                "compKey='" + compKey + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
