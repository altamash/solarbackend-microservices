package com.solaramps.loginservice.tenant.model.document;

import com.solaramps.loginservice.tenant.model.contract.Contract;
import com.solaramps.loginservice.tenant.model.contract.Organization;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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
    private String codeRefType;
    private Long codeRefId;
    private String docuName;
    private String notes;
    private String tags;
    private String format;
    private String uri;
    private String status;
    private Boolean visibilityKey;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "entity_id")
    private com.solaramps.loginservice.tenant.model.contract.Entity entity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
