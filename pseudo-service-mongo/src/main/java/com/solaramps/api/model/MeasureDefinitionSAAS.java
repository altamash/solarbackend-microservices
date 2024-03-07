package com.solaramps.api.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureDefinitionSAAS {

    private Long id;
    private String measure;
    private String code;
    private String format;
    private String uom;
    private Boolean pct;
    private String attributeIdRef;
    private Long attributeIdRefId; // TODO: Make OneToMany
    private Boolean locked;
    private Boolean mandatory; // not used
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
    private Boolean systemUsed; // Cannot be overridden
    private String notes;
    private Boolean visible;
    private Long relatedId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String apiResponseMsg;
}
