package com.solaramps.api.utils;

public interface AppConstants {

    String PROFILE_DEVELOPMENT = "dev";
    String PROFILE_STAGING = "stage";
    String PROFILE_PRE_PRODUCTION = "preprod";
    String PROFILE_PRODUCTION = "prod";

    final class COLLECTION_FIELDS {
        public static final String _id = "_id";
        public static final String ref = "$ref";
        public static final String measures = "measures";
        public static final String variant = "variant";
        public static final String subscription = "subscription";
        public static final String product = "product";

        public static final String by_product = "by_product";
        public static final String by_customer = "by_customer";



        public static final String product_group = "product_group";
        public static final String variant_group = "variant_group";
        public static final String version = "version";
        public static final String base_platform_product_id = "base_platform_product_id";
        public static final String downloaded_product_date = "downloaded_product_date";
        public static final String last_update_date = "last_update_date";
        public static final String PRODUCTS = "products";
        public static final String GARDENS = "gardens";
        public static final String SUBSCRIPTION_ID = "subscription_id";
        public static final String SUBSCRIPTION = "subscription";
        public static final String category = "category";
        public static final String sections = "sections";
        public static final String content = "content";
        public static final String section_name = "section_name";
        public static final String content_type = "content_type";
        public static final String code = "code";
        public static final String default_value = "default_value";
        public static final String system_indicator ="system_indicator";
        public static final String status ="status";
        public static final String ref_id = "ref_id";
    }

    final class CONTENT_TYPE {
        public static final String data = "data";
        public static final String site = "site";
        public static final String income = "income";
        public static final String assumptions = "assumptions";

    }

    final class PHYSICAL_LOCATION{
        public static final String id = "id";
        public static final String physicalLocationDTOs = "physicalLocationDTOs";

    }
}
