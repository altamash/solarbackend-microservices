package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuType;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocuVolMapRepository extends JpaRepository<DocuVolMap, Long> {

    @Query("SELECT map FROM DocuVolMap map" +
            " LEFT JOIN FETCH map.docuType type" +
            " LEFT JOIN FETCH map.docuLibraries" +
            " where map.docuType = :docuType")
    List<DocuVolMap> findAllByDocuType(DocuType docuType);

    @Query("SELECT map FROM DocuVolMap map" +
            " LEFT JOIN FETCH map.docuVolumes volumes" +
            " LEFT JOIN FETCH map.docuType" +
            " LEFT JOIN FETCH map.docuLibraries" +
            " where :docuVolume in elements(map.docuVolumes)")
    List<DocuVolMap> findAllByDocuVolume(DocuVolume docuVolume);

    Optional<DocuVolMap> findByDocuType(DocuType docuType);
}
