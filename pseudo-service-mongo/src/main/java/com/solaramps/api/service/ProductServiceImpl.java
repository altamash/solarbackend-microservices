package com.solaramps.api.service;

import com.solaramps.api.TenantHolder;
import com.solaramps.api.exception.SolarApiException;
import com.solaramps.api.model.MongoCustomerSubscriptionDTO;
import com.solaramps.api.utils.AppConstants;
import com.solaramps.api.utils.Constants;
import com.solaramps.api.utils.EProductParsers;
import com.solaramps.api.utils.Utility;
import com.solaramps.api.wrapper.CustomerSubscriptionMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();

    final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TenantHolder tenantHolder;
    @Autowired
    private WrapperService wrapperService;

    @Override
    public JSONArray showProducts() {
        List<Document> saasProducts = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find().into(new ArrayList<Document>());
        JSONArray products = new JSONArray();
        for (Document doc : saasProducts) {
            products.put(doc.toJson());
        }
        return products;
    }

    @Override
    public String saveOrUpdateProduct(String productObject, String reqType) throws SolarApiException {
        Document productDocument = Document.parse(productObject);
        productDocument.put("last_update_date", Utility.getDate(new Date(), Utility.SYSTEM_DATE_TIME_FORMAT));
        Document measuresDoc = (Document) productDocument.get("measures");
        Utility.measuresUpdate(measuresDoc, tenantHolder.getTenantId());

        if (Constants.REQUEST_TYPE_SAVE.equalsIgnoreCase(reqType)) {
            // productDocument.put("parser_code", "CSG_PRE_P_A");
            mongoTemplate.save(productDocument, Constants.COLLECTION_PRODUCTS);
            return "Product is saved successfully.";

        } else if (Constants.REQUEST_TYPE_UPDATE.equalsIgnoreCase(reqType)) {

            BasicDBObject filter = new BasicDBObject("_id", productDocument.get(AppConstants.COLLECTION_FIELDS._id));
            BasicDBObject updateObj = new BasicDBObject().append("$set", productDocument);
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            options.returnDocument(ReturnDocument.AFTER);
            options.upsert(true);
            mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).findOneAndUpdate(filter, updateObj, options);
            return "Product is updated successfully.";
        }
        return null;
    }

    @Override
    public String saveOrUpdateTenantProduct(String productObject, String reqType) throws SolarApiException {
        Document productDocument = Document.parse(productObject);
        productDocument.put(AppConstants.COLLECTION_FIELDS.last_update_date, Utility.getDate(new Date(), Utility.SYSTEM_DATE_TIME_FORMAT));
        productDocument.put(AppConstants.COLLECTION_FIELDS.downloaded_product_date, Utility.getDate(new Date(), Utility.SYSTEM_DATE_TIME_FORMAT));

        Bson matchStage = null;
        boolean parserFlag = false;
        boolean tempFlag = false;
        if (productDocument.get("parser_code") != null) {
            if (EProductParsers.get(productDocument.get("parser_code").toString()) != null) {
                parserFlag = true;
                Document measuresDoc = (Document) productDocument.get(AppConstants.COLLECTION_FIELDS.measures);
                Utility.measuresUpdate(measuresDoc, tenantHolder.getTenantId());
                matchStage = Aggregates.match(Filters.eq("base_platform_product_id.$id", new ObjectId(productDocument.get("_id").toString())));
            }

        } else if (productDocument.get("template_ind") != null) {
            tempFlag = true;
            matchStage = Aggregates.match(Filters.eq("template_ind", true));
        }

        if (parserFlag || tempFlag) {
            if (Constants.REQUEST_TYPE_SAVE.equalsIgnoreCase(reqType)) {
                String ver = getLatestVersion(matchStage);
                if (ver == null) {
                    productDocument.replace(AppConstants.COLLECTION_FIELDS.version, productDocument.get(AppConstants.COLLECTION_FIELDS.version).toString().concat(".0"));
                } else {
                    productDocument.replace(AppConstants.COLLECTION_FIELDS.version, Utility.upgradeVersion(ver));
                }
                Object baseId = productDocument.get(AppConstants.COLLECTION_FIELDS._id);
                productDocument.put(AppConstants.COLLECTION_FIELDS.base_platform_product_id, Utility.convertToDBRef(Constants.COLLECTION_PRODUCTS, baseId, null));
                productDocument.remove(AppConstants.COLLECTION_FIELDS._id);
                mongoTemplate.save(productDocument, Constants.COLLECTION_PRODUCTS);
                return "Product is saved successfully.";

            } else if (Constants.REQUEST_TYPE_UPDATE.equalsIgnoreCase(reqType)) {

                //productDocument.replace(AppConstants.COLLECTION_FIELDS.version, Utility.upgradeVersion(ver));
                BasicDBObject filter = new BasicDBObject(AppConstants.COLLECTION_FIELDS._id, productDocument.get(AppConstants.COLLECTION_FIELDS._id));
                BasicDBObject updateObj = new BasicDBObject().append("$set", productDocument);
                FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
                options.returnDocument(ReturnDocument.AFTER);
                options.upsert(true);
                mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).findOneAndUpdate(filter, updateObj, options);
                return "Product is updated successfully.";
            }
        } else {
            return "Parser Code does not exists.";
        }
        return null;
    }

    @Override
    public String showAllProducts() throws SolarApiException {
        List<Document> tenantProducts = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find().into(new ArrayList<Document>());
        /*JSONArray products = new JSONArray();
        for (Document doc : tenantProducts) {
            products.put(doc.toJson(codec));
        }*/
        List<String> products = new ArrayList<>();
        for (Document doc : tenantProducts) {
            products.add(doc.toJson(codec));
        }
        return products.toString();
    }

    @Override
    public String showProductById(String id) throws SolarApiException {
        BasicDBObject filter = new BasicDBObject("_id", new ObjectId(id));
        Document product = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find(filter).first();
        return product.toJson(codec);
    }

    @Override
    public String showProductByTmpInd(Boolean tmpInd) throws SolarApiException {
        BasicDBObject filter = new BasicDBObject("template_ind", tmpInd);
        List<Document> tenantProducts = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find(filter).into(new ArrayList<>());
        List<String> products = new ArrayList<>();
        for (Document doc : tenantProducts) {
            products.add(doc.toJson(codec));
        }
        return products.toString();
    }

    @Override
    public String createCollectionByProductIdInTenant(String requestType, String productObject) throws SolarApiException {
        Document gardenDocument = Document.parse(productObject);
        Boolean isProdGrpDoc = gardenDocument.get(AppConstants.COLLECTION_FIELDS.product_group).toString().contains(AppConstants.COLLECTION_FIELDS.ref);
        String productId = null;
        if (!isProdGrpDoc) {
            Object productGroup = gardenDocument.get(AppConstants.COLLECTION_FIELDS.product_group);
            productId = ((Document) productGroup).get(AppConstants.COLLECTION_FIELDS._id).toString();
        } else {
            productId = ((Document) gardenDocument.get(AppConstants.COLLECTION_FIELDS.product_group)).get("$id").toString();
        }

        Document measuresDoc = (Document) gardenDocument.get(AppConstants.COLLECTION_FIELDS.measures);
        Utility.measuresUpdate(measuresDoc, tenantHolder.getTenantId());

        Document newDataBlock = new Document();
        newDataBlock.put("ref_block_id", gardenDocument.get("block_definition_id"));
        newDataBlock.put("title", gardenDocument.get("block_definition_title"));

        if (Constants.REQUEST_TYPE_SAVE.equalsIgnoreCase(requestType)) {
            List<Document> dataBlocks = new ArrayList<Document>();
            dataBlocks.add(newDataBlock);
            gardenDocument.put("data_blocks", dataBlocks);

            if (gardenDocument.size() != 0) {
                if (!mongoTemplate.collectionExists(productId)) {
                    mongoTemplate.createCollection(productId);
                }
                gardenDocument.replace(AppConstants.COLLECTION_FIELDS.product_group, Utility.convertToDBRef(Constants.COLLECTION_PRODUCTS, productId, null));
                mongoTemplate.insert(gardenDocument, productId);
                return "Variant is added in product collection " + productId + " .";
            }

        } else if (Constants.REQUEST_TYPE_UPDATE.equalsIgnoreCase(requestType)) {
            List<Document> dataBlocks = (List<Document>) gardenDocument.get("data_blocks");
            if (dataBlocks != null) {
                dataBlocks.add(newDataBlock);
            }

            BasicDBObject filter = new BasicDBObject("_id", gardenDocument.get("_id"));
            BasicDBObject updateObj = new BasicDBObject().append("$set", gardenDocument);
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            options.returnDocument(ReturnDocument.AFTER);
            options.upsert(true);
            mongoTemplate.getCollection(productId).findOneAndUpdate(filter, updateObj, options);
            return "Variant is updated successfully.";
        }
        return null;
    }

    @Override
    public List<String> showGardensByProductId(String id) {
        List<Document> gardenDocuments = mongoTemplate.getCollection(id).find().into(new ArrayList<Document>());
        //JSONArray gardens = new JSONArray();
        List<String> products = new ArrayList<>();
        for (Document doc : gardenDocuments) {
            products.add(doc.toJson(codec));
        }
        return products;
    }

    @Override
    public String createCollectionByGardenIdInTenant(String productObject) {
        try {
            System.out.println(productObject);
            Document gardenDocument = Document.parse(productObject);

            if (gardenDocument.size() != 0) {
                Document productDocument = (Document) gardenDocument.get("garden_id");
                if (!mongoTemplate.collectionExists(productDocument.get("id").toString())) {
                    mongoTemplate.createCollection(productDocument.get("id").toString());
                }

                mongoTemplate.insert(gardenDocument, productDocument.get("id").toString());
                return "Garden is added in product collection " + productDocument.get("id").toString() + " .";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error is occurred.";
        }
        return null;
    }

    @Override
    public String addOrUpdateProduct(String productObject) {
        mongoTemplate.save(productObject, Constants.COLLECTION_PRODUCTS);
        return "Product is saved/updated successfully.";
    }

    @Override
    public String delete(String measuresObject) {
        mongoTemplate.remove(measuresObject, Constants.COLLECTION_PRODUCTS);
        return "Product is delete successfully.";
    }

    @Override
    public String createCollectionByVariantIdInTenant(String requestType, String subscriptionObject) throws SolarApiException {

        Document subsDocument = Document.parse(subscriptionObject);
        Boolean isProdGrpDoc = subsDocument.get(AppConstants.COLLECTION_FIELDS.product_group).toString().contains(AppConstants.COLLECTION_FIELDS.ref);
        String productId = Utility.getGroupId(subsDocument, isProdGrpDoc, AppConstants.COLLECTION_FIELDS.product_group);
        /*if (!isProdGrpDoc) {
            Object productGroup = subsDocument.get(AppConstants.COLLECTION_FIELDS.product_group);
            productId = ((Document) productGroup).get(AppConstants.COLLECTION_FIELDS._id).toString();
        } else {
            productId = ((Document) subsDocument.get(AppConstants.COLLECTION_FIELDS.product_group)).get("$id").toString();
        }*/

        Boolean isVariantGrpDoc = subsDocument.get(AppConstants.COLLECTION_FIELDS.variant_group).toString().contains(AppConstants.COLLECTION_FIELDS.ref);
        String variantId = Utility.getGroupId(subsDocument, isVariantGrpDoc, AppConstants.COLLECTION_FIELDS.variant_group);
        /*if (!isVariantGrpDoc) {
            Object variantGroup = subsDocument.get(AppConstants.COLLECTION_FIELDS.variant_group);
            variantId = ((Document) variantGroup).get(AppConstants.COLLECTION_FIELDS._id).toString();
        } else {
            variantId = ((Document) subsDocument.get(AppConstants.COLLECTION_FIELDS.variant_group)).get("$id").toString();
        }*/

        Document measuresDoc = (Document) subsDocument.get(AppConstants.COLLECTION_FIELDS.measures);
//        Utility.measuresUpdate(measuresDoc, tenantHolder.getTenantId());

        if (Constants.REQUEST_TYPE_SAVE.equalsIgnoreCase(requestType)) {
            BasicDBObject filter = new BasicDBObject("_id", new ObjectId(variantId));
            Document variantDoc = mongoTemplate.getCollection(productId).find(filter).first();

            if (subsDocument.size() != 0) {
                if (!mongoTemplate.collectionExists(variantId)) {
                    mongoTemplate.createCollection(variantId);
                }

                subsDocument.put("data_blocks", variantDoc.get("data_blocks"));
                List<Document> dataBlocks = (List<Document>) subsDocument.get("data_blocks");
                if (dataBlocks != null) {
                    dataBlocks.forEach(db -> {
                        db.put("block_collection_name", "");
                    });
                }

                subsDocument.replace(AppConstants.COLLECTION_FIELDS.product_group, Utility.convertToDBRef(Constants.COLLECTION_PRODUCTS, productId, null));
                subsDocument.replace(AppConstants.COLLECTION_FIELDS.variant_group, Utility.convertToDBRef(productId, variantId, null));

                Document subsId = mongoTemplate.insert(subsDocument, variantId);
                productMapping("", productId, variantId, subsId.get("_id").toString());
                return "Subscription is added in variant collection " + variantId + " .";
            }

        } else if (Constants.REQUEST_TYPE_UPDATE.equalsIgnoreCase(requestType)) {

            BasicDBObject filter = new BasicDBObject(AppConstants.COLLECTION_FIELDS._id, subsDocument.get(AppConstants.COLLECTION_FIELDS._id));
            BasicDBObject updateObj = new BasicDBObject().append("$set", subsDocument);
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            options.returnDocument(ReturnDocument.AFTER);
            options.upsert(true);
            mongoTemplate.getCollection(variantId).findOneAndUpdate(filter, updateObj, options);
            return "Subscription is updated in variant collection " + variantId + " .";
        }
        return null;
    }

    @Override
    public Long counterForBasePlatformProduct(String id) {
        BasicDBObject filter = new BasicDBObject(AppConstants.COLLECTION_FIELDS.base_platform_product_id, Utility.convertToDBRef(Constants.COLLECTION_PRODUCTS, new ObjectId(id), "saas"));
        Long counter = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).countDocuments(filter);
        return counter;
    }

    @Override
    public String counterForAllBasePlatformProduct() {
        List<Document> docs = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).aggregate(Arrays.asList(new Document("$group", new Document("_id", new Document(AppConstants.COLLECTION_FIELDS.base_platform_product_id, "$base_platform_product_id")).append("baseCount", new Document("$sum", 1L))))).into(new ArrayList<>());
        List<String> baseCount = new ArrayList<>();
        for (Document d : docs) {
            Object base = d.get("_id");
            baseCount.add(d.toJson(codec));
        }
        return baseCount.toString();
    }

    @Override
    public JSONArray showForBasePlatformProduct(String id) {
        BasicDBObject filter = new BasicDBObject(AppConstants.COLLECTION_FIELDS.base_platform_product_id, Utility.convertToDBRef(Constants.COLLECTION_PRODUCTS, new ObjectId(id), "saas"));
        List<Document> basePlatformDocuments = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find(filter).into(new ArrayList<Document>());
        JSONArray basePlatformRecords = new JSONArray();
        for (Document doc : basePlatformDocuments) {
            basePlatformRecords.put(doc.toJson(codec));
        }
        return basePlatformRecords;
    }

    @Override
    public List<CustomerSubscriptionMapping> getMappingsWithStaticValues(String subIds) {

        List<CustomerSubscriptionMapping> csm = new ArrayList<>();
        List<String> subscriptionIds = Arrays.stream(subIds.split(",")).map(id -> id.trim()).collect(Collectors.toList());

        for (String subId : subscriptionIds) {

            BasicDBObject prodMapFilter = new BasicDBObject(AppConstants.COLLECTION_FIELDS.subscription, subId);
            Document prodMapping = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS_MAPPING).find(prodMapFilter).first();

            if (prodMapping != null) {

                BasicDBObject varMapFilter = new BasicDBObject("_id", new ObjectId(prodMapping.get(AppConstants.COLLECTION_FIELDS.subscription).toString()));
                Document varMapping = mongoTemplate.getCollection(prodMapping.get(AppConstants.COLLECTION_FIELDS.variant).toString()).find(varMapFilter).first();

                Document subMeasuresDoc = (Document) varMapping.get(AppConstants.COLLECTION_FIELDS.measures);
                List<Document> subByCustDocs = (List<Document>) subMeasuresDoc.get(AppConstants.COLLECTION_FIELDS.by_customer);

                List<Document> filteredCust = null;
                if (subByCustDocs != null) {
                    filteredCust = subByCustDocs.stream().filter(cus -> cus.get("level") != null && "0".equalsIgnoreCase(cus.get("level").toString())).collect(Collectors.toList());
                }

                BasicDBObject filterVariant = new BasicDBObject("_id", new ObjectId(prodMapping.get(AppConstants.COLLECTION_FIELDS.variant).toString()));
                Document variant = mongoTemplate.getCollection(prodMapping.get(AppConstants.COLLECTION_FIELDS.product).toString()).find(filterVariant).first();
                Document measuresDoc = (Document) variant.get(AppConstants.COLLECTION_FIELDS.measures);
                List<Document> subByProdDocs = (List<Document>) measuresDoc.get(AppConstants.COLLECTION_FIELDS.by_product);

                if (subByProdDocs != null) {
                    subByProdDocs = subByProdDocs.stream().filter(cus -> cus.get("level") != null && "0".equalsIgnoreCase(cus.get("level").toString())).collect(Collectors.toList());
                    measuresDoc.replace(AppConstants.COLLECTION_FIELDS.by_product, subByProdDocs);
                }

                variant.put("subscription_id", prodMapping.get("subscription").toString());
                measuresDoc.remove(AppConstants.COLLECTION_FIELDS.by_customer);
                measuresDoc.put(AppConstants.COLLECTION_FIELDS.by_customer, filteredCust);
                //return variant.toJson(codec).toString();
                CustomerSubscriptionMapping customerSubscriptionMapping = wrapperService.fromSubscriptionToCSM(variant.toJson(codec).toString());
                csm.add(customerSubscriptionMapping);
            }
        }
        return csm;
    }

    @Override
    public List<CustomerSubscriptionMapping> getMappingsForCalculationOrderedBySequence(String subIds) {

        List<CustomerSubscriptionMapping> csm = new ArrayList<>();
        List<String> subscriptionIds = Arrays.stream(subIds.split(",")).map(id -> id.trim()).collect(Collectors.toList());

        for (String subId : subscriptionIds) {

            BasicDBObject prodMapFilter = new BasicDBObject("subscription", subId);
            Document prodMapping = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS_MAPPING).find(prodMapFilter).first();

            BasicDBObject varMapFilter = new BasicDBObject("_id", new ObjectId(prodMapping.get("subscription").toString()));
            Document varMapping = mongoTemplate.getCollection(prodMapping.get("variant").toString()).find(varMapFilter).first();

            Document subMeasuresDoc = (Document) varMapping.get("measures");
            List<Document> subByCustDocs = (List<Document>) subMeasuresDoc.get("by_customer");

            List<Document> filteredCust = null;
            if (subByCustDocs != null) {
                filteredCust = subByCustDocs.stream().filter(cus -> cus.get("level") != null && "1".equalsIgnoreCase(cus.get("level").toString())).collect(Collectors.toList());
            }

            BasicDBObject filterVariant = new BasicDBObject("_id", new ObjectId(prodMapping.get("variant").toString()));
            Document variant = mongoTemplate.getCollection(prodMapping.get("product").toString()).find(filterVariant).first();
            Document measuresDoc = (Document) variant.get("measures");
            List<Document> subByProdDocs = (List<Document>) measuresDoc.get("by_product");

            if (subByProdDocs != null) {
                subByProdDocs = subByProdDocs.stream().filter(cus -> (cus.get("level") != null && "1".equalsIgnoreCase(cus.get("level").toString()))).collect(Collectors.toList());
                measuresDoc.replace("by_product", subByProdDocs);
            }

            variant.put("subscription_id", prodMapping.get("subscription").toString());
            measuresDoc.remove("by_customer");
            measuresDoc.put("by_customer", filteredCust);
            //return variant.toJson(codec).toString();
            CustomerSubscriptionMapping customerSubscriptionMapping = wrapperService.fromSubscriptionToCSM(variant.toJson(codec).toString());
            csm.add(customerSubscriptionMapping);
        }
        return csm;
    }

    @Override
    public List<String> showAllCommunityGardens() throws JsonProcessingException, SolarApiException {
        List<Document> tenantProducts = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find().into(new ArrayList<Document>());
        tenantProducts = tenantProducts.stream().filter(prod -> (prod.get(AppConstants.COLLECTION_FIELDS.category).toString().equalsIgnoreCase("Community Solar Garden"))).collect(Collectors.toList());
        // List<GardenDetail> gardensDetail = new ArrayList<>();
        List<String> gardenDetailList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Document doc : tenantProducts) {
            String productId = doc.get("_id").toString();
            List<Document> gardenDocuments = mongoTemplate.getCollection(productId).find().into(new ArrayList<Document>());
            for (Document gardenDoc : gardenDocuments) {
                //gardenDoc.toJson(codec);
                Document measuresDoc = (Document) gardenDoc.get("measures");
                List<Document> byCustDocs = (List<Document>) measuresDoc.get("by_customer");
                List<Document> byProdctDocs = (List<Document>) measuresDoc.get("by_product");

                if (byProdctDocs != null) {
                    byProdctDocs = byProdctDocs.stream().filter(prod -> (prod.get("self_register") != null) && ("true".equalsIgnoreCase(prod.get("self_register").toString()))).collect(Collectors.toList());
                    measuresDoc.replace("by_product", byProdctDocs);
                }
                if (byCustDocs != null) {
                    byCustDocs = byCustDocs.stream().filter(cus -> (cus.get("self_register") != null) && ("true".equalsIgnoreCase(cus.get("self_register").toString()))).collect(Collectors.toList());
                    measuresDoc.replace("by_customer", byCustDocs);
                }
                gardenDoc.replace("measures", measuresDoc);
                gardenDetailList.add(gardenDoc.toJson(codec).toString());
            }

//            gardensDetail.add(GardenDetail.builder().productId(productId).gardenDetail(gardenDocuments.).build());
        }
        return gardenDetailList;
    }

    @Override
    public String getSubscriptionMappingById(String subId) throws SolarApiException {

        BasicDBObject prodMapFilter = new BasicDBObject("subscription", subId);
        Document prodMapping = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS_MAPPING).find(prodMapFilter).first();

        BasicDBObject subFilter = new BasicDBObject("_id", new ObjectId(subId));
        Document subMapping = mongoTemplate.getCollection(prodMapping.get("variant").toString()).find(subFilter).first();
        Document finalDoc = new Document();

        if (subMapping != null) {
            BasicDBObject filterVariant = new BasicDBObject("_id", new ObjectId(prodMapping.get("variant").toString()));
            Document variantDoc = mongoTemplate.getCollection(prodMapping.get("product").toString()).find(filterVariant).first();
            finalDoc.put("variant", variantDoc);
            finalDoc.put("subscription", subMapping);
        }
        return finalDoc.toJson(codec);
    }

    public static void main(String[] args) {
        Map<String, Object> criteria = new HashMap<>();
//        criteria.put("active", "true");
//        criteria.put("measures.by_customer.default_value", "0.1");
//        criteria.put("measures.by_customer.code", "S_DSCP");
        criteria.put("subscription", "62b2cff4609ad53891c35355");
//        criteria.put("name", "David -V (SRC076610)");
        for (Document document : getDocuments("products_mapping", criteria)) {
//        for (Document document : getDocuments2("62b2cd5b9415714e92d91653", criteria)) {
//        for (Document document : getDocuments2("62b203579415714e92d91652", criteria)) {
//        for (Document document : getDocuments3("products", new HashMap<>(), new HashMap<>())) {
            System.out.println(document.toJson(new DocumentCodec(MongoClientSettings.getDefaultCodecRegistry(), new BsonTypeClassMap())));
        }

        new ProductServiceImpl().updateDocuments("products_mapping");
//        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameterMap()
//        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getQueryString()
    }

    private static List<Document> getDocuments(String collectionName, Map<String, Object> criteria) {
        List<Document> documents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://devstg:YfUKD1M3FZG10SYY@devstgdb.xl8rzoa.mongodb.net/test")) {
            MongoDatabase database = mongoClient.getDatabase("ec1001");
            BasicDBObject criteriaObject = new BasicDBObject();
            criteria.entrySet().forEach(set -> criteriaObject.put(set.getKey(), set.getValue()));
            FindIterable<Document> document = database.getCollection(collectionName).find(criteriaObject);
            MongoCursor<Document> cursor = document.cursor();
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        }
        return documents;
    }

    private List<Document> updateDocuments(String collectionName) {
        List<Document> documents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://devstg:YfUKD1M3FZG10SYY@devstgdb.xl8rzoa.mongodb.net/test")) {
            MongoDatabase database = mongoClient.getDatabase("ec1001");
            FindIterable<Document> collection = database.getCollection(collectionName).find();
            MongoCursor<Document> cursor = collection.cursor();
            MongoTemplate template = new MongoTemplate(mongoClient, "ec1001");
            while (cursor.hasNext()) {
                Document document = cursor.next();
                document.put("product_active", "ACTIVE");
                document.put("variant_active", "ACTIVE");
                document.put("subscription_active", "ACTIVE");
                documents.add(template.save(document));
            }
            documents = template.save(documents);
        }
        return documents;
    }

    private static List<Document> getDocuments2(String collectionId, Map<String, Object> criteria) {
        List<Document> documents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://devstg:YfUKD1M3FZG10SYY@devstgdb.xl8rzoa.mongodb.net/test")) {
            MongoDatabase database = mongoClient.getDatabase("ec1001");
            MongoCollection<Document> collection = database.getCollection(collectionId);
            List<Bson> bSons = new ArrayList<>();
            criteria.entrySet().forEach(set -> bSons.add(Aggregates.match(Filters.eq(set.getKey(), set.getValue()))));
            collection.aggregate(bSons).forEach(documents::add);
        }
        return documents;
    }

    private static List<Document> getDocuments3(String collectionName, Map<String, Object> criteria,
                                                Map<String, Map<String, Object>> criteriaMap) {
        List<Document> documents = new ArrayList<>();
        //try (MongoClient mongoClient = MongoClients.create("mongodb://mongo-sidevdb:7S3gZclkbQevRpTb2lvDnIgTGZhfxTZ3Fct4zQN4HuUEsCQa4ktEdXv1L6hTSUGSoHVyOKIcmOZhE2VUntmzGA%3D%3D@mongo-sidevdb.mongo.cosmos.azure.com:10255/?ssl=true&retrywrites=false&maxIdleTimeMS=120000&appName=@mongo-sidevdb@")) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://devstg:YfUKD1M3FZG10SYY@devstgdb.xl8rzoa.mongodb.net/test")) {
            MongoDatabase database = mongoClient.getDatabase("ec1001");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            List<Bson> bSons = new ArrayList<>();
            criteria.entrySet().forEach(set -> bSons.add(Aggregates.match(Filters.eq(set.getKey(), set.getValue()))));
            collection.aggregate(bSons).forEach(documents::add);
            documents.forEach(doc -> criteriaMap.entrySet().forEach(set -> {
                if ("products".equals(collectionName) && "variants".equals(set.getKey()) ||
                        "variants".equals(collectionName) && "subscriptions".equals(set.getKey())) {
                    doc.put(set.getKey(), getDocuments3(doc.get(AppConstants.COLLECTION_FIELDS._id).toString(), set.getValue(), criteriaMap));
                } else {
                    doc.put(set.getKey(), getDocuments3(collectionName, set.getValue(), criteriaMap));
                }
            }));
        }
        return documents;
    }

    @Override
    public String getVariantMappingById(String variantId) throws SolarApiException {
        BasicDBObject prodMapFilter = new BasicDBObject("variant", variantId);
        Document prodMapping = null;

        try {
            prodMapping = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS_MAPPING).find(prodMapFilter).first();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Document finalDoc = new Document();
        BasicDBObject filterVariant = new BasicDBObject("_id", new ObjectId(variantId));
        Document variantDoc = mongoTemplate.getCollection(prodMapping.get("product").toString()).find(filterVariant).first();
        finalDoc.put("variant", variantDoc);

        if (prodMapping == null) {
            finalDoc.put("subscriptions", new ArrayList<Document>());
        } else {
            List<Document> subMappings = mongoTemplate.getCollection(prodMapping.get("variant").toString()).find().into(new ArrayList<>());
            if (subMappings != null) {
                finalDoc.put("subscriptions", subMappings);
            }
        }
        return finalDoc.toJson(codec);
    }

    @Override
    public List<String> getVariantsByCategoryAndStatus(String categoryName, String status) throws SolarApiException {
        BasicDBObject prodFilter = null;
        List<Document> prodList = null;
        if (categoryName != null) {
            prodFilter = new BasicDBObject("category", categoryName);
            prodList = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find(prodFilter).into(new ArrayList<>());
        } else {
            prodList = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find().into(new ArrayList<>());
        }
        List<Document> allVariants = new ArrayList<>();
        List<String> variants = new ArrayList<>();

        for (Document doc : prodList) {
            BasicDBObject variantFilter = new BasicDBObject("status", status != null ? status : "active");
            List<Document> resultList = mongoTemplate.getCollection(doc.get("_id").toString()).find(variantFilter).projection(new BasicDBObject("measures", 0)).into(new ArrayList<>());
            allVariants.addAll(resultList);
        }

        if (allVariants.size() != 0) {
            allVariants.stream().peek(a -> variants.add(a.toJson(codec))).collect(Collectors.toList());
        }
        return variants;
    }

    private void productMapping(String level, Object... params) {
        Document prodMap = new Document();
        prodMap.put("product", (String) params[0]);
        prodMap.put("variant", (String) params[1]);
        prodMap.put("subscription", (String) params[2]);
        mongoTemplate.save(prodMap, Constants.COLLECTION_PRODUCTS_MAPPING);
    }

    private String getLatestVersion(Bson matchStage) throws SolarApiException {
        //Bson matchStage = Aggregates.match(Filters.eq("base_platform_product_id.$id", new ObjectId(id)));
        Bson sortStage = Aggregates.sort(Sorts.descending("_id"));
        List<Bson> pipeline = new LinkedList<>();
        pipeline.add(matchStage);
        pipeline.add(sortStage);
        Document downloadedProduct = null;
        try {
            downloadedProduct = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).aggregate(pipeline).first();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return downloadedProduct != null ? downloadedProduct.get("version").toString() : null;
    }

    @Override
    public List<String> createSubscriptionCollectionByVariantIdInTenant(String requestType, String subsObjectList) throws JsonProcessingException {
        List<String> response = new ArrayList<>();
        TypeReference<List<LinkedHashMap>> typeRef = new TypeReference<List<LinkedHashMap>>() {
        };
        List<LinkedHashMap> map = new ObjectMapper().readValue(subsObjectList, typeRef);
        String requestId = Utility.getUniqueIdentifier();
        for (LinkedHashMap obj : map) {
            obj.putIfAbsent("requestId", requestId);
            String json = new Gson().toJson(obj, Map.class).toString();
            response.add(createCollectionByVariantIdInTenant(requestType, json));
        }
        return response;
    }

    @Override
    public List<MongoCustomerSubscriptionDTO> showAllSubscription() throws JsonProcessingException, SolarApiException {
        List<Document> tenantProducts = mongoTemplate.getCollection(Constants.COLLECTION_PRODUCTS).find().into(new ArrayList<Document>());
        tenantProducts = tenantProducts.stream().filter(prod -> (prod.get(AppConstants.COLLECTION_FIELDS.category).toString().equalsIgnoreCase("Community Solar Garden"))).collect(Collectors.toList());
        List<Document> subscriptionDetailList = new ArrayList<>();
        Map<String, MongoCustomerSubscriptionDTO> subsByRequestId = new HashMap<>();

        for (Document doc : tenantProducts) {
            String productId = doc.get("_id").toString();
            List<Document> gardenDocuments = mongoTemplate.getCollection(productId).find().into(new ArrayList<Document>());
            for (Document gardenDoc : gardenDocuments) {
                String gardenId = gardenDoc.get("_id").toString();
                List<Document> subscriptionDocument = mongoTemplate.getCollection(gardenId).find().into(new ArrayList<Document>());
                subscriptionDocument.stream().forEach(sub -> {
                    if (sub.get("requestId") != null) {
                        Document measuresDoc = (Document) sub.get("measures");
                        measuresDoc.put("by_product", ((Document) gardenDoc.get("measures")).get("by_product"));
                        subscriptionDetailList.add(sub);
                    }
                });
            }
        }
        for (Document sub : subscriptionDetailList) {
            String requestId = sub.get("requestId") != null ? sub.get("requestId").toString() : null;
            String status = sub.get("status") != null ? sub.get("status").toString() : null;
            String requestStatus = (status != null && !status.equalsIgnoreCase("draft")) ? "Complete" : "inComplete";
            if (requestId != null) {
                Document measuresDoc = (Document) sub.get("measures");
                List<Document> byCustDocs = (List<Document>) measuresDoc.get("by_customer");
                if (byCustDocs != null) {
                    Long contractId = 0L;
                    String customerName = "";
                    Long requestedCapacity = 0L;
                    Optional<Document> requestedCapacityDoc = byCustDocs.stream().filter(subObj -> subObj.get("code") != null && subObj.get("code").toString().equalsIgnoreCase("S_KWDC")).findFirst();
                    if (requestedCapacityDoc.isPresent())
                        requestedCapacity = Long.valueOf(requestedCapacityDoc.get().get("default_value").toString());
                    Optional<Document> customerNameDoc = byCustDocs.stream().filter(subObj -> subObj.get("code") != null && subObj.get("code").toString().equalsIgnoreCase("CN")).findFirst();
                    if (customerNameDoc.isPresent()) customerName = customerNameDoc.get().getString("default_value");
                    Optional<Document> contractDoc = byCustDocs.stream().filter(subObj -> subObj.get("code") != null && subObj.get("code").toString().equalsIgnoreCase("S_CONTRACT")).findFirst();
                    if (contractDoc.isPresent())
                        contractId = Long.valueOf(contractDoc.get().get("default_value").toString());

                    JSONObject jsonObject = new JSONObject();

                    if (requestId != null) {
                        if (!subsByRequestId.isEmpty() && subsByRequestId.containsKey(requestId)) {
                            MongoCustomerSubscriptionDTO customerSubscriptionDTO = subsByRequestId.get(requestId);
                            if (requestStatus.equalsIgnoreCase("Complete") && customerSubscriptionDTO.getRequestStatus().equalsIgnoreCase("Complete"))
                                requestStatus = "Complete";
                            else requestStatus = "inComplete";

                            customerSubscriptionDTO.getSubscriptions().add(sub.toJson(codec).toString());
                            customerSubscriptionDTO.setKwRequested(customerSubscriptionDTO.getKwRequested() + Long.valueOf(requestedCapacity));
                            customerSubscriptionDTO.setRequestStatus(requestStatus);
                            customerSubscriptionDTO.setRequestId(requestId);
                        } else {
                            List<String> subJson = new ArrayList<>();
                            subJson.add(sub.toJson(codec).toString());
                            subsByRequestId.put(requestId, MongoCustomerSubscriptionDTO.builder().requestId(requestId).subscriptions(subJson).customerName(customerName).contractId(contractId).requestStatus(requestStatus).kwRequested(Long.valueOf(requestedCapacity)).build());
                        }
                    }
                }

            }
        }
        return subsByRequestId.values().stream().collect(Collectors.toList());
    }

}
