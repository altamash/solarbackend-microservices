package com.solaramps.api.tenant.service.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;

public interface DocuTypeServiceExt {
    BaseResponse getAllDocuTypes(int pageNumber, Integer pageSize, String sort);
}
