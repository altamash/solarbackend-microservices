package com.solaramps.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoCustomerSubscriptionMasterDTO<T> implements Serializable {

private List<MongoCustomerSubscriptionDTO> mongoCustomerSubscriptionDTO;



}