package com.solaramps.api.service;

import com.solaramps.api.utils.AppConstants;
import com.solaramps.api.wrapper.CustomerSubscriptionMapping;
import com.solaramps.api.wrapper.RateCodeMapping;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WrapperServiceImpl implements WrapperService {

    @Override
    public CustomerSubscriptionMapping fromSubscriptionToCSM(String productObject) {

        Document doc = Document.parse(productObject);
        CustomerSubscriptionMapping customerSubscriptionMapping = CustomerSubscriptionMapping.builder()
                .title(doc.get("title") != null ? doc.get("title").toString() : null)
                .name(doc.get("name") != null ? doc.get("name").toString() : null)
                .version(doc.get("version") != null ? doc.get("version").toString() : null)
                .status(doc.get("status") != null ? doc.get("status").toString() : null)
                .category(doc.get("billing_model") != null ? doc.get("billing_model").toString() : null)
                .chargingCode(doc.get("charging_code") != null ? doc.get("charging_code").toString() : null)
                .billingCycle(doc.get("billing_cycle") != null ? doc.get("billing_cycle").toString() : null)
                .billingFrequency(doc.get("billing_frequency") != null ? doc.get("billing_frequency").toString() : null)
                .maxTenure(doc.get("max_tenure") != null ? doc.get("max_tenure").toString() : null)
                .defaultValidationRuleGroup(doc.get("default_validation_rule_group") != null ? doc.get("default_validation_rule_group").toString() : null)
                .taxGroup(doc.get("tax_group") != null ? doc.get("tax_group").toString() : null)
                .prepaymentCode(doc.get("prepayment_code") != null ? doc.get("prepayment_code").toString() : null)
                .description(doc.get("description") != null ? doc.get("description").toString() : null)
                .parserCode(doc.get("parser_code") != null ? doc.get("parser_code").toString() : null)
                .variantName(doc.get("variant_name") != null ? doc.get("variant_name").toString() : null)
                .subscriptionId(doc.get("subscription_id") != null ? doc.get("subscription_id").toString() : null)
                .variantId(doc.get("_id").toString())
                .productId(((Document) doc.get(AppConstants.COLLECTION_FIELDS.product_group)).get("$id").toString()).build();

        Document measuresDoc = (Document) doc.get("measures");
        List<Document> byCustomer = (List<Document>) measuresDoc.get("by_customer");
        List<RateCodeMapping> rateCodeMappings = new ArrayList<>();
        if (byCustomer != null) {
            byCustomer.forEach(csm -> {
                rateCodeMappings.add(RateCodeMapping.builder()
                        .rateCode(csm.get("code").toString())
                        .value(csm.get("default_value") != null ? csm.get("default_value").toString() : null)
                        .sequence(Long.valueOf(csm.get("seq") != null ? csm.get("seq").toString() : "0"))
                        .locked(csm.get("locked") != null ? Boolean.parseBoolean(csm.get("locked").toString()) : null)
                        .mandatory(csm.get("mandatory") != null ? Boolean.parseBoolean(csm.get("mandatory").toString()) : null)
                        .pct(csm.get("PCT") != null ? Boolean.parseBoolean(csm.get("PCT").toString()) : null)
                        .system(csm.get("system") != null ? csm.get("system").toString() : null)
                        .visible(csm.get("visible") != null ? Boolean.parseBoolean(csm.get("visible").toString()) : null)
                        .description(csm.get("description") != null ? csm.get("description").toString() : null)
                        .build());
            });
        }

        List<Document> byProduct = (List<Document>) measuresDoc.get("by_product");
        if (byProduct != null) {
            byProduct.forEach(csm -> {
                rateCodeMappings.add(RateCodeMapping.builder()
                        .rateCode(csm.get("code").toString())
                        .value(csm.get("default_value") != null ? csm.get("default_value").toString() : null)
                        .sequence(Long.valueOf(csm.get("seq") != null ? csm.get("seq").toString() : "0"))
                        .locked(csm.get("locked") != null ? Boolean.parseBoolean(csm.get("locked").toString()) : null)
                        .mandatory(csm.get("mandatory") != null ? Boolean.parseBoolean(csm.get("mandatory").toString()) : null)
                        .pct(csm.get("PCT") != null ? Boolean.parseBoolean(csm.get("PCT").toString()) : null)
                        .system(csm.get("system") != null ? csm.get("system").toString() : null)
                        .visible(csm.get("visible") != null ? Boolean.parseBoolean(csm.get("visible").toString()) : null)
                        .description(csm.get("description") != null ? csm.get("description").toString() : null)
                        .build());
            });
        }
        rateCodeMappings.sort(Comparator.comparing(RateCodeMapping::getSequence));
        customerSubscriptionMapping.setRateCodes(rateCodeMappings);
        return customerSubscriptionMapping;
    }
}
