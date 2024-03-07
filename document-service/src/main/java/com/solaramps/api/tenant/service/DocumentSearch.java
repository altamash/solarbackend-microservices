package com.solaramps.api.tenant.service;

import com.solaramps.api.tenant.mapper.BaseResponse;

import java.util.List;

public interface DocumentSearch {

    BaseResponse search(String string, Long docuTypeId, List<Long> docuVolumeIds, List<Long> docuModuleIds,
                        String fromDateString, String toDateString, int pageNumber, Integer pageSize, String sort,
                        Integer sortDirection);

    BaseResponse getSummaryByDocuType(Long docuTypeId);

    BaseResponse getSummaryByDocuVolume(Long docuVolumeId);
}
