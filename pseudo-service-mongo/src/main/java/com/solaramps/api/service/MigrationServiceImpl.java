package com.solaramps.api.service;

import com.solaramps.api.TenantHolder;
import com.solaramps.api.model.MeasureDefinitionSAASDTO;
import com.solaramps.api.model.SubscriptionRateMatrixDetailDTO;
import com.solaramps.api.model.SubscriptionRateMatrixHeadDTO;
import com.solaramps.api.controller.MigrationController;
import com.solaramps.api.service.process.APIConstants;
import com.solaramps.api.service.process.PrerequisitesFactory;
import com.solaramps.api.service.saasmigration.MigrationAPIBody;
import com.solaramps.api.service.saasmigration.User;
import com.solaramps.api.utils.Constants;
import com.solaramps.api.utils.WebUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@Service
public class MigrationServiceImpl implements MigrationService {

    final static Logger LOGGER = LoggerFactory.getLogger(MigrationController.class);
    CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TenantHolder tenantHolder;
    @Autowired
    private ProductService productService;

    /*@Override
    public String fromSqlSAASToMongoSAAS(String type) {

        *//*try {
            if ("measures".equalsIgnoreCase(type)) {

                List<MeasureDefinitionSAAS> measureDefinitionSAAS = measureDefinitionSAASService.findAll();

                if (measureDefinitionSAAS.size() != 0) {
*//**//*
                List<Document> documents = measureDefinitionSAAS.stream()
                        .map(jsonStr -> Document.parse(new ObjectMapper().writeValueAsString(jsonStr)))
                        .collect(Collectors.toList());*//**//*
                    List<Document> measureDocuments = new ArrayList<>();
                    measureDefinitionSAAS.forEach(m -> {
                        try {
                            measureDocuments.add(Document.parse(new ObjectMapper().writeValueAsString(m)));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });

                    if (measureDocuments.size() != 0) {
                        if (!saasMongoTemplate.collectionExists("measures")) {
                            saasMongoTemplate.createCollection("measures");
                        }
                        saasMongoTemplate.getCollection(Constants.COLLECTION_MEASURES).insertMany(measureDocuments);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*//*
        return "saved";
    }*/

    @Override
    public String fromSqlSAASToMongoSAAS(String type) {
        Map<String, APIConstants> constants = PrerequisitesFactory.getAPIPrerequisites("RDBMS").getConstants();
        APIConstants signUrl = constants.get("SIGN_IN");

        try {
            if (Constants.COLLECTION_MEASURES.equalsIgnoreCase(type)) {
                APIConstants measuresUrl = constants.get("FIND_ALL_MEASURES_PROD");
                Map<String, List<String>> headers = new HashMap<>();
                String requestBodyJsonString = new ObjectMapper().writeValueAsString(MigrationAPIBody.builder()
                        .userName(Constants.RDBMS_USERNAME)
                        .passCode(Constants.RDBMS_PASSCODE).build());

                headers.put("content-type", Arrays.asList("application/json;charset=UTF-8"));

                ResponseEntity<User> loginResponse = WebUtils.submitRequest(signUrl.getMethod(), signUrl.getUrl(),
                        requestBodyJsonString, headers, User.class);

                String jwtToken = "Bearer ".concat(loginResponse.getBody().getJwtToken());
                headers.put("Authorization", Arrays.asList(jwtToken));

                ResponseEntity<MeasureDefinitionSAASDTO[]> measuresResponse = WebUtils.submitRequest(measuresUrl.getMethod(), measuresUrl.getUrl(),
                        null, headers, MeasureDefinitionSAASDTO[].class);

                MeasureDefinitionSAASDTO[] measuresSAASList = measuresResponse != null ? measuresResponse.getBody() : null;
                List<Document> measureDocuments = new ArrayList<>();
                Arrays.stream(measuresSAASList).forEach(md -> {
                    try {
                        //measureDocuments.add(Document.parse(new ObjectMapper().writeValueAsString(md));
                        Gson gson = new Gson();
                        String obj = gson.toJson(md);
                        obj = obj.replaceAll("id", "ref_id");
                        //need to add self_register and s_contract
                        measureDocuments.add(Document.parse(obj));
                        System.out.println(Document.parse(obj));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                if (!measureDocuments.isEmpty()) {
                    if (!mongoTemplate.collectionExists(Constants.COLLECTION_MEASURES)) {
                        mongoTemplate.createCollection(Constants.COLLECTION_MEASURES);
                    }
                    //mongoTemplate.getCollection(Constants.COLLECTION_MEASURES).insertMany(measureDocuments);
                    return "RDBMS measures have been migrated to MONGODB.";
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            LOGGER.warn(e.getMessage());
            return "error";
        }
        return null;
    }

    @Override
    public String variants() {
        Map<String, APIConstants> constants = PrerequisitesFactory.getAPIPrerequisites("RDBMS").getConstants();
        APIConstants signTenantUrl = constants.get("SIGN_IN_TENANT_PROD");
        APIConstants variantUrl = constants.get("VARIANTS_BY_CODE");
        APIConstants measuresByVariantUrl = constants.get("MEASURES_BY_VARIANT");
        APIConstants subscriptionRateMatrixUrl = constants.get("SUBSCRIPTION_RATE_MATRIX");

        Map<String, List<String>> headers = new HashMap<>();
        String requestBodyJsonString = null;
        try {
            requestBodyJsonString = new ObjectMapper().writeValueAsString(MigrationAPIBody.builder()
                    .userName(Constants.RDBMS_USERNAME_PROD)
                    .password(Constants.RDBMS_PASSWORD).build());

            headers.put("content-type", Arrays.asList("application/json;charset=UTF-8"));
            String tenantId = tenantHolder.getTenantId().replaceAll("ec", "");
            headers.put("Comp-Key", Arrays.asList(tenantId));

            ResponseEntity<User> loginResponse = WebUtils.submitRequest(signTenantUrl.getMethod(), signTenantUrl.getUrl(),
                    requestBodyJsonString, headers, User.class);

            String jwtToken = "Bearer ".concat(loginResponse.getBody().getJwtToken());
            headers.put("Authorization", Arrays.asList(jwtToken));

            ResponseEntity<SubscriptionRateMatrixHeadDTO[]> variantResponse = WebUtils.submitRequest(variantUrl.getMethod(), variantUrl.getUrl().concat("CSGF"),
                    null, headers, SubscriptionRateMatrixHeadDTO[].class);
            SubscriptionRateMatrixHeadDTO[] variantList = variantResponse != null ? variantResponse.getBody() : null;

            Arrays.stream(variantList).forEach(variant -> {
                ResponseEntity<SubscriptionRateMatrixHeadDTO> measuresResponse = WebUtils.submitRequest(measuresByVariantUrl.getMethod(),
                        measuresByVariantUrl.getUrl().concat(variant.getSubscriptionTemplate()),
                        null, headers, SubscriptionRateMatrixHeadDTO.class);
                SubscriptionRateMatrixHeadDTO measureList = measuresResponse != null ? measuresResponse.getBody() : null;

                tenantHolder.setTenantId("saas");
                List<Document> saasMeasures = mongoTemplate.getCollection("measures").find().into(new ArrayList<>());
                List<Document> byProduct = new ArrayList<>();
                saasMeasures.forEach(m -> {
                    Optional<SubscriptionRateMatrixDetailDTO> SubscriptionRateMatrixDetailDTO = measureList.getSubscriptionRateMatrixDetails().stream()
                            .filter(c -> c.getSubscriptionCode().equalsIgnoreCase(m.get("code").toString())).findFirst();

                    if (measureList.getSubscriptionRateMatrixDetails().size() != 0) {
                        measureList.getSubscriptionRateMatrixDetails().forEach(c -> {
                            if (c.getRateCode().equalsIgnoreCase(m.get("code").toString())) {
                                byProduct.add(m);
                            }
                        });
                    }
                });

                BasicDBObject productIdFilter = new BasicDBObject("_id", new ObjectId("62b203579415714e92d91652"));
                tenantHolder.setTenantId("ec1001");
                Document gardenDoc = mongoTemplate.getCollection("products").find(productIdFilter).first();
                Document measuresDoc = (Document) gardenDoc.get("measures");
                measuresDoc.remove((List<Document>) measuresDoc.get("by_product"));
                measuresDoc.put("by_product", byProduct);

                System.out.println(gardenDoc);
                gardenDoc.remove("_id");
                gardenDoc.put("product_group", productIdFilter);
                productService.createCollectionByProductIdInTenant("save", gardenDoc.toJson(codec).toString());
            });

/*
            ResponseEntity<SubscriptionRateMatrixHeadDTO[]> subsResponse = WebUtils.submitRequest(subscriptionRateMatrixUrl.getMethod(), subscriptionRateMatrixUrl.getUrl(),
                    null, headers, SubscriptionRateMatrixHeadDTO[].class);

            SubscriptionRateMatrixHeadDTO[] subsRateMatrixList = subsResponse != null ? subsResponse.getBody() : null;
            List<Document> measureDocuments = new ArrayList<>();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
