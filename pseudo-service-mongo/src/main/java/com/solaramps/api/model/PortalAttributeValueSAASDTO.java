package com.solaramps.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortalAttributeValueSAASDTO {

    private Long id;
    private String attributeName;
    private Long attributeId;
    private String attributeValue;
    private Integer sequenceNumber;
    private String parentReferenceValue;
    private String description;
}
