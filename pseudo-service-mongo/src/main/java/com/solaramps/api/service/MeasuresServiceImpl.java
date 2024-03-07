package com.solaramps.api.service;

import com.solaramps.api.utils.Constants;
import org.bson.Document;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeasuresServiceImpl implements MeasuresService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String saveMeasures(String measuresObject) {
        Document measuresDocument = Document.parse(measuresObject);
        mongoTemplate.insert(measuresDocument, Constants.COLLECTION_MEASURES);
        return "Measure is added successfully.";
    }

    @Override
    public String addOrUpdateMeasures(String measuresObject) {
        mongoTemplate.save(measuresObject, Constants.COLLECTION_MEASURES);
        return "Measure is saved/updated successfully.";
    }

    public String delete(String measuresObject) {
        mongoTemplate.remove(measuresObject, Constants.COLLECTION_MEASURES);
        return "Measure is delete successfully.";
    }

    @Override
    public JSONArray showMeasures() {
        List<Document> saasMeasures = mongoTemplate.getCollection(Constants.COLLECTION_MEASURES).find().into(new ArrayList<Document>());
        JSONArray measures = new JSONArray();
        for (Document doc : saasMeasures) {
            //doc.remove("_id");
            measures.put(doc.toJson());
        }
        return measures;
    }
}
