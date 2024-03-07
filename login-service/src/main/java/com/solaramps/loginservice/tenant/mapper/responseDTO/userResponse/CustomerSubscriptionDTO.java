package com.solaramps.loginservice.tenant.mapper.responseDTO.userResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.solaramps.loginservice.tenant.mapper.user.address.AddressDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSubscriptionDTO {

    private Long id;
    private String subscriptionType;
    private String subscriptionTemplate;
    private Long subscriptionRateMatrixId;
    private Long userAccountId;
    private Date startDate;
    private Date endDate;
    private String subscriptionStatus;
    private String billStatus;
    private String arrayLocationRef;
    private AddressDTO address;
    private SubscriptionRateMatrixHeadDTO subscriptionRateMatrixHead;
    private List<CustomerSubscriptionMappingDTO> customerSubscriptionMappings;
    private Date terminationDate;
    private Date closedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String terminationReason;
    private String rollValue;
    private Boolean markedForDeletion;
    private Long contractId; // subscription parent contract
}
