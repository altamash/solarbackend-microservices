package com.solaramps.api.service;

import com.solaramps.api.wrapper.CustomerSubscriptionMapping;

public interface WrapperService {

    CustomerSubscriptionMapping fromSubscriptionToCSM(String productObject);

}
