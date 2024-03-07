package com.solaramps.api.utils;

import com.solaramps.api.model.BaseResponse;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.apache.commons.codec.digest.DigestUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Utility {

    public static final String SYSTEM_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date getDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static BasicDBObject convertToDBRef(String collectionName, Object baseId, String db) {
        BasicDBObject dbRefObject = new BasicDBObject();
        dbRefObject.put("$ref", collectionName);
        dbRefObject.put("$id", baseId);
        if (db != null) {
            dbRefObject.put("$db", db);
        }
        return dbRefObject;
    }

    public static String upgradeVersion(String version) {
        String[] versionValue = version.split("\\.");
        Integer verCounter = Integer.valueOf(versionValue[2]) + 1;
        String versionUpgrade = versionValue[0].concat(".").concat(versionValue[1]).concat(".").concat(verCounter.toString());
        return versionUpgrade;
    }

    public static void measuresUpdate(Document measuresDoc, String db) {
        measuresUpdate(measuresDoc, db, "by_product");
        measuresUpdate(measuresDoc, db, "by_customer");
    }

    public static void measuresUpdate(Document measuresDoc, String db, String key) {
        List<Document> prodMDoc = (List<Document>) measuresDoc.get(key);
        if (prodMDoc != null) {
            prodMDoc.forEach(measure -> {
                Boolean isDoc = measure.get("_id").toString().contains("$ref");
                if (!isDoc) {
                    measure.replace("_id", Utility.convertToDBRef(Constants.COLLECTION_MEASURES, measure.get("_id"), db));
                }
            });
        }
    }

    public static String getGroupId(Document subsDocument, Boolean isGrpDoc, String grpName) {
        String grpId = null;
        if (!isGrpDoc) {
            Object productGroup = subsDocument.get(grpName);
            grpId = ((Document) productGroup).get(AppConstants.COLLECTION_FIELDS._id).toString();
        } else {
            grpId = ((Document) subsDocument.get(grpName)).get("$id").toString();
        }
        return grpId;
    }

    public static BaseResponse<Object> setBaseResponse(int code, String message) {
        return BaseResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static String getUniqueIdentifier(){
        String uniqueID = UUID.randomUUID().toString();
        String ts = String.valueOf(System.currentTimeMillis());
        return DigestUtils.sha1Hex(ts + uniqueID);
    }

    public static void main(String[] args) {
        upgradeVersion("1.0");
    }

    public static BaseResponse<Object> setBaseResponse(int code, List messages) {
        return BaseResponse.builder()
                .code(code)
                .messages(messages)
                .build();
    }
}
