package com.solaramps.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoCustomerSubscriptionDTO<T> implements Serializable {
    private Long kwRequested;
    private String customerName;
    private Long contractId;
    private String requestStatus;
    private String requestId;
    private List<String> subscriptions;



}