package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuLibrary;
import com.solaramps.api.tenant.model.docu.DocuModule;
import com.solaramps.api.tenant.model.docu.DocuVolMap;
import com.solaramps.api.tenant.model.docu.DocuVolume;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DocuLibrarySpecification {

    static Specification<DocuLibrary> textInAllColumns(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return new Specification<DocuLibrary>() {
            @Override
            public Predicate toPredicate(Root<DocuLibrary> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root.fetch("roles", JoinType.LEFT);
                return criteriaBuilder.or(root.getModel().getDeclaredSingularAttributes().stream().filter(a -> {
                            if (a.getJavaType().getSimpleName().equalsIgnoreCase("string")) {
                                return true;
                            } else {
                                return false;
                            }
                        }).map(a -> criteriaBuilder.like(root.get(a.getName()), finalText)
                              ).toArray(Predicate[]::new)
                                         );
            }
        };
    }

    static Specification<DocuLibrary> withFieldValue(String field, Object value) {

        return new Specification<DocuLibrary>() {
            @Override
            public Predicate toPredicate(Root<DocuLibrary> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root.fetch("roles", JoinType.LEFT);
                return criteriaBuilder.equal(root.get(field), value);
            }
        };
    }

    /*static Specification<DocuLibrary> withDocuIds(List<Long> docuIds) {

        return new Specification<DocuLibrary>() {
            @Override
            public Predicate toPredicate(Root<DocuLibrary> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root.fetch("docuVolMaps", JoinType.LEFT);
                Path<DocuVolMap> path = root.get("docuId");
                return root.get("docuId").in(docuIds);
            }
        };
    }*/

    static Specification<DocuLibrary> findByDocuType(Long docuTypeId) {

        return (root, query, criteriaBuilder) -> {
//                root.fetch("docuVolMap", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("docuTypeId").get("id"), docuTypeId);
        };
    }

    static Specification<DocuLibrary> findByDocuVolumeIn(List<Long> docuVolumeIds) {
        return (root, query, criteriaBuilder) -> {
//            root.fetch("docuVolMap", JoinType.LEFT).fetch("docuVolumes");
//            query.orderBy(criteriaBuilder.asc(root.<DocuLibrary>get("docuName")));
            return root.<DocuVolMap>get("docuVolMap").<Set<DocuVolume>>get("docuVolumes").<Long>get("id").in(docuVolumeIds);
        };
    }

    static Specification<DocuLibrary> findByDocuModuleIn(List<Long> docuModuleIds) {
        return (root, query, criteriaBuilder) -> {
//            root.fetch("docuVolMap", JoinType.LEFT).fetch("docuVolumes").fetch("docuModule");
//            query.orderBy(criteriaBuilder.asc(root.<DocuLibrary>get("moduleName")));
            return root.<DocuVolMap>get("docuVolMap").<Set<DocuVolume>>get("docuVolumes").<DocuModule>get("docuModule").get("id").in(docuModuleIds);
        };
    }

    static Specification<DocuLibrary> findByFromDateGE(LocalDateTime fromDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt").as(LocalDate.class), fromDate.toLocalDate());
    }

    static Specification<DocuLibrary> findByToDateLT(LocalDateTime fromDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("createdAt").as(LocalDate.class), fromDate.toLocalDate());
    }
}
