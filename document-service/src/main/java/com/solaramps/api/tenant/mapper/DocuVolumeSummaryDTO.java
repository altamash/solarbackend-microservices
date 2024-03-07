package com.solaramps.api.tenant.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DocuVolumeSummaryDTO {
    private String volumeName;
    private int numberOfDocumentTypes;
    private int maxFileSizeAllowed;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<DocuTypeDTO> documentTypes;
    private List<SupportedFileTypeCount> supportedFileTypes;
    private long totalDocuments;
}
