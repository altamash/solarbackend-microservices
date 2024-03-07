package com.solaramps.api.tenant.controller.v1;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.service.DocumentSearch;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("SearchController")
@RequestMapping(value = "/docu")
public class SearchController {

    private final DocumentSearch documentSearch;

    public SearchController(DocumentSearch documentSearch) {
        this.documentSearch = documentSearch;
    }

    @GetMapping("/search")
    public BaseResponse search(@RequestParam(required = false) String string,
                               @RequestParam(required = false) Long docuTypeId,
                               @RequestParam(required = false) List<Long> docuModuleIds,
                               @RequestParam(required = false) List<Long> docuVolumeIds,
                               @RequestParam(required = false) @Parameter(description = "yyyy-MM-dd") String fromDateString,
                               @RequestParam(required = false) @Parameter(description = "yyyy-MM-dd") String toDateString,
                               @RequestParam int pageNumber,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false, defaultValue = "-1") @Parameter(description = "volumeName, moduleName, documentName, uploadedOn etc") String sort,
                               @RequestParam(required = false) @Parameter(description = "1 for ASC (default); 2 for DESC") Integer sortDirection) {
        return documentSearch.search(string, docuTypeId, docuVolumeIds, docuModuleIds, fromDateString, toDateString, pageNumber, pageSize, sort, sortDirection);
    }

    @GetMapping("/docuTypeSummary")
    public BaseResponse getSummaryByDocuType(@RequestParam Long docuTypeId) {
        return documentSearch.getSummaryByDocuType(docuTypeId);
    }

    @GetMapping("/docuVolumeSummary")
    public BaseResponse getSummaryByDocuVolume(@RequestParam Long docuVolumeId) {
        return documentSearch.getSummaryByDocuVolume(docuVolumeId);
    }
}
