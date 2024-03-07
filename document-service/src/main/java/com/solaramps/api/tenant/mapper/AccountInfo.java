package com.solaramps.api.tenant.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfo {

    private String skuName;
    private String accountKind;
}
