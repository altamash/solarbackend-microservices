package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuVolume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocuVolumeRepository extends JpaRepository<DocuVolume, Long> {
//    DocuVolume findByDocuModule(DocuModule docuModule);

    List<DocuVolume> findByIdIn(List<Long> ids);
}
