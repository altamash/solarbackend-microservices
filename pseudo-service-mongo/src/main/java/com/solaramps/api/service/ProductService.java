package com.solaramps.api.service;

import com.solaramps.api.exception.SolarApiException;
import com.solaramps.api.model.MongoCustomerSubscriptionDTO;
import com.solaramps.api.wrapper.CustomerSubscriptionMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;

import java.util.List;

public interface ProductService {
    JSONArray showProducts() throws SolarApiException;
    String saveOrUpdateProduct(String productObject, String reqType) throws SolarApiException;
    String saveOrUpdateTenantProduct(String productObject, String reqType) throws SolarApiException;
    String showAllProducts() throws SolarApiException;
    String showProductById(String id) throws SolarApiException;
    String showProductByTmpInd(Boolean tmpInd) throws SolarApiException;
    String createCollectionByProductIdInTenant(String requestType, String productObject) throws SolarApiException;
    List<String> showGardensByProductId(String id) throws SolarApiException;
    String createCollectionByGardenIdInTenant(String productObject) throws SolarApiException;
    String addOrUpdateProduct(String productObject) throws SolarApiException;
    String delete(String productObject) throws SolarApiException;
    String createCollectionByVariantIdInTenant(String requestType, String subscriptionObject) throws SolarApiException;
    Long counterForBasePlatformProduct(String id) throws SolarApiException;
    //JSONArray counterForAllBasePlatformProduct(String ids);
    String counterForAllBasePlatformProduct() throws SolarApiException;
    JSONArray showForBasePlatformProduct(String id) throws SolarApiException;
    List<CustomerSubscriptionMapping> getMappingsWithStaticValues(String subsObject) throws SolarApiException;
    List<CustomerSubscriptionMapping> getMappingsForCalculationOrderedBySequence(String subsObject) throws SolarApiException;
    List<String> showAllCommunityGardens() throws JsonProcessingException, SolarApiException;
    List<String> createSubscriptionCollectionByVariantIdInTenant(String requestType, String subsObjectList) throws JsonProcessingException;
    List<MongoCustomerSubscriptionDTO> showAllSubscription() throws JsonProcessingException, SolarApiException;
    String getSubscriptionMappingById(String subId) throws SolarApiException;
    String getVariantMappingById(String variantId) throws SolarApiException;
    List<String> getVariantsByCategoryAndStatus(String category, String status) throws SolarApiException;
}
