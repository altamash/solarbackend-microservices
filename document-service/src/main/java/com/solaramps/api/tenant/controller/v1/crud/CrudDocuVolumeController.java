package com.solaramps.api.tenant.controller.v1.crud;

import com.solaramps.api.tenant.mapper.BaseResponse;
import com.solaramps.api.tenant.mapper.DocuVolumeDTO;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import com.solaramps.api.tenant.service.crud.DocuVolumeService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docuVolume/crud")
public class CrudDocuVolumeController implements CrudController<DocuVolume> {

    private final DocuVolumeService docuVolumeService;

    public CrudDocuVolumeController(DocuVolumeService docuVolumeService) {
        this.docuVolumeService = docuVolumeService;
    }

    @Hidden
    @Override
    public BaseResponse<?> save(DocuVolume obj) {
        return null;
    }

    @Hidden
    @Override
    public BaseResponse<?> update(DocuVolume obj) {
        return null;
    }

    @Override
    public BaseResponse<?> getAll() {
        return docuVolumeService.getAllResponse();
    }

    @Override
    public BaseResponse<?> getById(Long id) {
        return docuVolumeService.getByIdResponse(id);
    }

    @Override
    public BaseResponse<?> delete(Long id) {
        return docuVolumeService.deleteByIdResponse(id);
    }

    @Hidden
    @Override
    public BaseResponse<?> deleteAll() {
        return docuVolumeService.deleteAllResponse();
    }

    @PostMapping("/save")
    BaseResponse save(@RequestBody DocuVolumeDTO docuVolumeDTO) {
        return docuVolumeService.saveResponse(docuVolumeDTO);
    }

    @PutMapping("/update")
    BaseResponse update(@RequestBody DocuVolumeDTO docuVolumeDTO) {
        return docuVolumeService.updateResponse(docuVolumeDTO);
    }
}
