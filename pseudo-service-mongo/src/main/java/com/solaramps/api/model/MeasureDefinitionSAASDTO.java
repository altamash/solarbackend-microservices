package com.solaramps.api.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureDefinitionSAASDTO {

    private Long id;
    private String measure;
    private String code;
    private String format;
    private String uom;
    private Boolean pct;
    private String attributeIdRef;
    private Long attributeIdRefId;
    private Boolean locked;
    private Boolean mandatory;
    private String relatedMeasure;
    private String alias;
    private String type;
    private String regModule;
    private Long regModuleId;
    private String validationRule;
    private String validationParams;
    private String actions;
    private String visibilityLevel;
    private String compEvents;
    private Boolean systemUsed;
    private String notes;
//    private String portalAttributeValues;
    private List<PortalAttributeValueSAASDTO> portalAttributeValues;
    private Boolean visible;
    private Long relatedId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
