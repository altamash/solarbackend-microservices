package com.solaramps.api.controller;

import com.solaramps.api.exception.SolarApiException;
import com.solaramps.api.model.BaseResponse;
import com.solaramps.api.model.MongoCustomerSubscriptionMasterDTO;
import com.solaramps.api.service.ProductService;
import com.solaramps.api.utils.Utility;
import com.solaramps.api.wrapper.CustomerSubscriptionMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/product")
public class ProductController {

    final static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/saas/saveProduct/{reqType}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity<?> saveOrUpdateProduct(@RequestParam("productObject") String productObject,
                                                 @PathVariable String reqType,
                                                 @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        BaseResponse<Object> baseResponse = Utility.setBaseResponse(HttpStatus.OK.value(),
                productService.saveOrUpdateProduct(productObject, reqType));
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/tenant/saveProduct/{reqType}")
    public ResponseEntity<?> saveOrUpdateTenantProduct(@RequestParam("productObject") String productObject,
                                                       @PathVariable String reqType,
                                                       @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        BaseResponse<Object> baseResponse = Utility.setBaseResponse(HttpStatus.OK.value(),
                productService.saveOrUpdateTenantProduct(productObject, reqType));
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/showAllProducts")
    public ResponseEntity<String> showAllProducts(@RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        try {
            return new ResponseEntity<>(productService.showAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/showProductById/{id}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity showProductById(@PathVariable String id, @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.showProductById(id));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/showProductByTmpInd/{tmpInd}")
    public ResponseEntity showProductByTmpInd(@PathVariable Boolean tmpInd, @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.showProductByTmpInd(tmpInd).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @PostMapping("/createCollectionByProductIdInTenant/{requestType}")
    public ResponseEntity<?> createCollectionByProductIdInTenant(@PathVariable String requestType,
                                                                 @RequestParam("productObject") String productObject,
                                                                 @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        String response = productService.createCollectionByProductIdInTenant(requestType, productObject);
        BaseResponse<Object> baseResponse = Utility.setBaseResponse(HttpStatus.OK.value(), response);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/showGardensByProductId/{productId}")
    public ResponseEntity showGardensByProductId(@PathVariable String productId, @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.showGardensByProductId(productId).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @PostMapping("/createCollectionByGardenIdInTenant/{requestType}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity<?> createCollectionByGardenIdInTenant(@PathVariable String requestType,
                                                                @RequestParam("subsObject") String subsObject,
                                                                @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        BaseResponse<Object> baseResponse = Utility.setBaseResponse(HttpStatus.OK.value(),
                productService.createCollectionByVariantIdInTenant(requestType, subsObject));
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/tenant/counterForBasePlatformProduct/{id}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity counterForBasePlatformProduct(@PathVariable String id,
                                                        @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        return ResponseEntity.ok(productService.counterForBasePlatformProduct(id));

    }

    @GetMapping("/tenant/counterForAllBasePlatformProduct")
    public ResponseEntity counterForAllBasePlatformProduct(@RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.counterForAllBasePlatformProduct());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tenant/showForBasePlatformProduct/{id}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity showForBasePlatformProduct(@PathVariable String id,
                                                     @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.showForBasePlatformProduct(id).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tenant/getMappingsWithStaticValues/{subId}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public List<CustomerSubscriptionMapping> getMappingsWithStaticValues(@PathVariable String subId,
                                                                         @RequestHeader("Tenant-id") String tenantId) {
        return productService.getMappingsWithStaticValues(subId);
    }

    @GetMapping("/tenant/getMappingsForCalculationOrderedBySequence/{subId}")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public List<CustomerSubscriptionMapping> getMappingsForCalculationOrderedBySequence(@PathVariable String subId,
                                                                                        @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        return productService.getMappingsForCalculationOrderedBySequence(subId);
    }

    @GetMapping("/tenant/getSubscriptionMappingById/{subId}")
    public ResponseEntity getSubscriptionMappingById(@PathVariable String subId,
                                                     @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.getSubscriptionMappingById(subId).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tenant/getVariantMappingById/{variantId}")
    public ResponseEntity getVariantMappingById(@PathVariable String variantId,
                                                @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.getVariantMappingById(variantId).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tenant/getVariantsByCategoryAndStatus")
    public ResponseEntity getVariantsByCategoryAndStatus(@RequestParam(required = false) String category,
                                                         @RequestParam(required = false) String status,
                                                         @RequestHeader("Tenant-id") String tenantId) {
        try {
            return ResponseEntity.ok(productService.getVariantsByCategoryAndStatus(category, status).toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/showAllGardens")
    //@ApiImplicitParam(name = "Tenant-id", value = "Tenant Schema", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "Tenant id")
    public ResponseEntity showAllGardens(@RequestHeader("Tenant-id") String tenantId) throws SolarApiException, JsonProcessingException {
        return ResponseEntity.ok(productService.showAllCommunityGardens().toString());
    }

    @PostMapping("/createSubscriptionCollectionByGardenIdInTenant/{requestType}")
    public ResponseEntity<?> createSubscriptionCollectionByGardenIdInTenant(@PathVariable String requestType,
                                                                            @RequestParam("subsObjectList") String subsObjectList,
                                                                            @RequestHeader("Tenant-id") String tenantId) throws SolarApiException {
        BaseResponse<Object> baseResponse = null;
        try {
            baseResponse = Utility.setBaseResponse(HttpStatus.OK.value(),
                    productService.createSubscriptionCollectionByVariantIdInTenant(requestType, subsObjectList));
            return ResponseEntity.ok(baseResponse);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/showAllSubscription")
    public MongoCustomerSubscriptionMasterDTO showAllSubscription(@RequestHeader("Tenant-id") String tenantId) throws SolarApiException, JsonProcessingException {
        return MongoCustomerSubscriptionMasterDTO.builder().mongoCustomerSubscriptionDTO(productService.showAllSubscription()).build();
    }


}
