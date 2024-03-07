package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuLibrary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocuLibraryRepository extends JpaRepository<DocuLibrary, Long> {
    List<DocuLibrary> findAllByDocuIdIn(List<Long> ids);

    Page<DocuLibrary> findAll(Pageable pageable);

    Page<DocuLibrary> findAll(Specification<DocuLibrary> spec, Pageable pageable);

    @Query("SELECT count(doc) from DocuLibrary doc where doc.docuTypeId.id = :docuTypeId")
    Long findDocuCountByDocuTypeId(Long docuTypeId);

    @Query("SELECT count(doc) from DocuLibrary doc where doc.docuVolMap.id in :docuVolMapId")
    Long findDocuCountByDocuVolMapIds(List<Long> docuVolMapId);
}
