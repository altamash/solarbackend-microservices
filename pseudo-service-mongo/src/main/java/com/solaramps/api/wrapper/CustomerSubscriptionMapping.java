package com.solaramps.api.wrapper;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubscriptionMapping {

    private String title;
    private String name;
    private String version;
    private String status;
    private String category;
    private String code ;
    private String billingModel;
    private String chargingCode;
    private String billingCycle;
    private String billingFrequency ;
    private String maxTenure;
    private String defaultValidationRuleGroup;
    private String taxGroup ;
    private String prepaymentCode ;
    private String description;
    private String parserCode;
    private String variantName;
    private String subscriptionId;
    private String variantId;
    private String productId;
    private List<RateCodeMapping> rateCodes;

}
