package com.solaramps.api.tenant.controller.v1;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuMeasureDTO;
import com.solaramps.api.tenant.mapper.DocuMeasureIdDTO;
import com.solaramps.api.tenant.service.DocumentManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("ManagementController")
@RequestMapping(value = "/docu")
public class ManagementController {

    private final DocumentManagement documentManagement;

    public ManagementController(DocumentManagement documentManagement) {
        this.documentManagement = documentManagement;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "To upload file and optionally update docu_library record with docuLibraryId")
    public BaseResponse uploadDocument(@RequestPart(value = "file") MultipartFile file,
//                                       @RequestParam(value = "fileName") @Parameter(description = "File name with extension (can be different from actual file name)") String fileName,
                                       @RequestParam(value = "allowedFormats", required = false) @Parameter(description = "text/html, application/json etc") List<String> allowedFormats,
                                       @RequestParam(value = "docuLibraryId"
//                                               , required = false
                                       ) Long docuLibraryId,
//                                       @RequestParam(value = "storage") @Schema(allowableValues = {"Staging", "Production"}, defaultValue = "Staging") String storage,
//                                       @RequestParam(value = "container") @Parameter(description = "dev/stage/preprod/prod etc") String container,
                                       @RequestParam(value = "storagePathCode") @Parameter(description = "Code for directory path inside storage container (eg PRJDOC)") String storagePathCode,
                                       @RequestParam(value = "relativeUrl", required = false, defaultValue = "false") @Parameter(description = "Truncate base url if true") boolean relativeUrl,
                                       @RequestHeader("Comp-Key") Long compKey) {
        return documentManagement.uploadDocument(file, allowedFormats, compKey, docuLibraryId, storagePathCode, relativeUrl);
    }

    @PostMapping(value = "/addOrUpdateDocuLibrary")
    public BaseResponse addOrUpdateDocuLibrary(@RequestBody @Parameter(description = "Note that this is a partial update. Null (null) if included is a valid attribute value") DocuMeasureDTO docuMeasureDTO,
                                               @RequestHeader("Comp-Key") Long compKey) {
        return documentManagement.addOrUpdateDocuLibrary(docuMeasureDTO.getDocuLibraryId(), docuMeasureDTO, compKey);
    }

    @GetMapping("/downloadDocument")
    public BaseResponse downloadDocument(@RequestParam Long docuLibraryId) {
        return documentManagement.downloadDocument(docuLibraryId);
    }

    @GetMapping("/downloadDocuments")
    public BaseResponse downloadDocuments(@RequestParam List<Long> docuLibraryIds) {
        return documentManagement.downloadDocuments(docuLibraryIds);
    }

    @DeleteMapping("/remove")
    public BaseResponse removeDocument(@RequestParam Long docuLibraryId,
                                       @RequestParam @Parameter(description = "Must be in the format project_id,section_id,measure_id") String measureIdHash,
                                       @RequestHeader("Comp-Key") Long compKey) {
        return documentManagement.softDeleteDocument(docuLibraryId, measureIdHash, compKey);
    }

    @DeleteMapping("/removeDocuments")
    public BaseResponse removeDocuments(@RequestParam @Parameter(description = "measureId Must be in the format project_id,section_id,measure_id")
                                            List<DocuMeasureIdDTO> docuMeasureIdDTOs,
                                       @RequestHeader("Comp-Key") Long compKey) {
        return documentManagement.softDeleteDocuments(docuMeasureIdDTOs, compKey);
    }
}
