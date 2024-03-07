package com.solaramps.api.tenant.model;

import com.solaramps.api.tenant.model.docu.DocuLibrary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "contract")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String contractName;
    @Column(nullable = false)
    private String contractType;
    private Long masterAgreementContractId;
    private String status;
    private Boolean isRenewable;
    private Boolean isDocAttached;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private Boolean primaryIndicator;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "entity_id")
    private Entity entity;
    @OneToMany(mappedBy = "contract", cascade = CascadeType.MERGE)
    private List<DocuLibrary> docuLibraries;
    @OneToMany(mappedBy = "contract", cascade = CascadeType.MERGE)
    private List<UserLevelPrivilege> userLevelPrivileges;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Long activeScoreId; // this will contain softCreditCheck tbl id
    private Boolean isSigned;
    private Long verifiedBy; // this will contain entity tbl id

}
