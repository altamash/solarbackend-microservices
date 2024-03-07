package com.solaramps.api.tenant.controller.v1;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolMapDTO;
import com.solaramps.api.tenant.service.DocumentConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController("ConfigurationController")
@RequestMapping(value = "/docu/conf")
public class ConfigurationController {

    private final DocumentConfiguration documentConfiguration;

    public ConfigurationController(DocumentConfiguration documentConfiguration) {
        this.documentConfiguration = documentConfiguration;
    }

    @PostMapping
    BaseResponse saveDocumentTypeVolumeMap(@RequestBody DocuVolMapDTO docuVolMapDTO) {
        return documentConfiguration.saveDocumentTypeVolumeMap(docuVolMapDTO);
    }

    @PutMapping
    BaseResponse updateDocumentTypeVolumeMap(@RequestBody DocuVolMapDTO docuVolMapDTO) {
        return documentConfiguration.updateDocumentTypeVolumeMap(docuVolMapDTO);
    }

    @GetMapping("/docuVolMapId/{docuVolMapId}")
    BaseResponse getDocumentTypeVolumeMap(Long docuVolMapId) {
        return documentConfiguration.getDocumentTypeVolumeMap(docuVolMapId);
    }

    @GetMapping("/mapDocumentWithTypeAndVolume")
    public BaseResponse mapDocumentWithTypeAndVolume(Long docuLibraryId, Long docVolMapId) {
        return documentConfiguration.mapDocumentWithTypeAndVolume(docuLibraryId, docVolMapId);
    }
}
