package com.solaramps.api.service;

import org.json.JSONArray;

public interface MeasuresService {
    String saveMeasures(String measuresObject);
    String addOrUpdateMeasures(String measuresObject);
    String delete(String measuresObject);
    JSONArray showMeasures();
}
