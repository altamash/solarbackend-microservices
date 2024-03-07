package com.solaramps.api.tenant.model.docu;

import com.solaramps.api.tenant.model.Contract;
import com.solaramps.api.tenant.model.Organization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "docu_library")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocuLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docuId;
    private String docuType;
//    @MapsId
    @OneToOne
    @JoinColumn(name = "docu_type_id")
    private DocuType docuTypeId;
    private String codeRefType;
    private String codeRefId;
    private String docuName;
    private String notes;
    private String tags;
    private String size;
    private String format;
    private String uri;
    private String status;
    private Boolean visibilityKey;
    private Long compKey;
    private Long resourceInterval;

    private Boolean lockedInd;
    private Boolean linkToMeasure;
    @Column(length = 15)
    private String state; // DRAFT, PUBLISHED
    @Column(length = 50)
    private String docuAlias;
    // private String docuVolMapId // TODO: to be checked with Shahryar
//    private List<String> tags;

    @Column(name="digitally_signed")
    private String digitallySigned;

    @Column(name="sign_ref_no")
    private String signRefNo;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "entity_id")
    private com.solaramps.api.tenant.model.Entity entity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    /*@ManyToMany(targetEntity = DocuVolMap.class)
    @JoinTable(name = "docu_library_docu_vol_map",
            joinColumns = @JoinColumn(name = "docu_library_id"),
            inverseJoinColumns = @JoinColumn(name = "docu_vol_map_id"))
    private Set<DocuVolMap> docuVolMaps;*/

//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "docu_vol_map_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "docu_vol_map_id")
    private DocuVolMap docuVolMap; // TODO: add in crud

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /*public void addDocVolMap(DocuVolMap docuVolMap) {
        if (docuVolMap != null) {
            this.docuVolMaps.add(docuVolMap);
        }
    }

    public void removeDocVolMap(DocuVolMap docuVolMap) {
        this.docuVolMaps.remove(docuVolMap);
    }*/

}
