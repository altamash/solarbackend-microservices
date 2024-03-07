package com.solaramps.api.commons.module.storage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfo {

    private String skuName;
    private String accountKind;
}
