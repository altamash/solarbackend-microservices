package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocuMeasureDTO {

    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("section_id")
    private String sectionId;
    @JsonProperty("measure_id")
    private String measureId;
    private String code; // SLA
    private String format; // DOCUMENT
    private String measure; // Service Level Agreement
    private Boolean locked;
    private String notes;
    private String type; // ADV
    private String flag;
    // all new measures following the document strategy should add these tags and remove the attributes not present above e.g. uom is deleted for docu
    // a new collection by name of measure_formats to be defined in saas where measure to be discussed later.
    @JsonProperty("allowed_formats")
    private List<String> allowedFormats; // [application/pdf;application/docx]; // or if all formats are allowed then it will have a *
    private String hint;
    @JsonProperty("docu_format")
    private String docuFormat; // sync from docu_library; if the document is mentioned in the measure definition then only those formats are allowed.
    // e.g. application/pdf other wise any format can be upload. its only 1 format or any format not comma separated.
    @JsonProperty("docu_alias")
    private String docuAlias; // this field also needs to be added to docu_library
    @JsonProperty("docu_library_id")
    private Long docuLibraryId;
    private String uri;
        // in case of any data collision between docu library and measure data e.g. docu format or alias; the docu_library content will prevail.
}


