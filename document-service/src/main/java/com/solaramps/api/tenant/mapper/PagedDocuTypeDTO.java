package com.solaramps.api.tenant.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PagedDocuTypeDTO {
    long totalItems;
    List<DocuTypeDTO> documents;
}
