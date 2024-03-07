package com.solaramps.api.service;

import com.solaramps.api.utils.Constants;
import org.bson.Document;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParsersServiceImpl implements ParsersService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String save(List<String> parsers) {

        if (!mongoTemplate.collectionExists(Constants.COLLECTION_PARSERS)) {
            mongoTemplate.createCollection(Constants.COLLECTION_PARSERS);
        }

        List<Document> parserDocuments = parsers.stream().map(p-> Document.parse(p)).collect(Collectors.toList());
        mongoTemplate.getCollection(Constants.COLLECTION_PARSERS).insertMany(parserDocuments);
        return "Parsers are added successfully.";
    }
    @Override
    public String saveOrUpdateParsers(String parsersObject) {
        mongoTemplate.save(parsersObject, Constants.COLLECTION_PARSERS);
        return "Parser is saved/updated successfully.";
    }
    @Override
    public String delete(String parsersObject) {
        mongoTemplate.remove(parsersObject, Constants.COLLECTION_PARSERS);
        return "Parser is delete successfully.";
    }

    @Override
    public JSONArray showParsers() {
        List<Document> saasParsers = mongoTemplate.getCollection(Constants.COLLECTION_PARSERS).find().into(new ArrayList<Document>());
        JSONArray parsers = new JSONArray();
        for (Document doc : saasParsers) {
            parsers.put(doc.toJson());
        }
        return parsers;
    }
}
