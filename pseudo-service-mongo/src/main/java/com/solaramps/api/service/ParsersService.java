package com.solaramps.api.service;

import org.json.JSONArray;

import java.util.List;

public interface ParsersService {
    String save(List<String> parsers);
    String saveOrUpdateParsers(String parsersObject);
    String delete(String parsersObject);
    JSONArray showParsers();
}
