package com.solaramps.api.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRateMatrixHeadDTO {

    private Long id;
    private String subscriptionCode;
    private String subscriptionTemplate;
    private Boolean active;
    private List<SubscriptionRateMatrixDetailDTO> subscriptionRateMatrixDetails;
}
