package com.solaramps.api.tenant.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupportedFileTypeCount {
    private String name;
    private Integer number;
}
