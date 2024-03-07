package com.solaramps.api.wrapper;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateCodeMapping {

    private String rateCode;
    private String value;
    private Integer level;
    private Long sequence;
    private String description;
    private Boolean mandatory;
    private Boolean locked;
    private Boolean visible;
    private Boolean pct;
    private String system;
}
