package com.solaramps.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionRateMatrixDetailDTO {

    private Long id;
    private Long subscriptionRateMatrixId;
    private String subscriptionCode;
    private String rateCode;
    private String defaultValue;
    private Integer level;
    private Integer sequenceNumber;
    private Boolean mandatory;
    private Boolean maintainBillHistory;
    private String flags;
    private Boolean allowOthersToEdit;
    //private FinancialBillingDefinitionDTO financialBillingDefinition;
    //private MeasureDefinitionTenantDTO measureDefinition;
    private Boolean systemUsed; // If it is used anywhere in the calculation logic (administrative)
    private Boolean varyByCustomer; // If yes, present in subscription mapping
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
