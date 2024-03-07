package com.solaramps.loginservice.tenant.model.contract;

import com.solaramps.loginservice.tenant.model.document.DocuLibrary;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@javax.persistence.Entity
@Table(name = "entity")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String entityName;
    private String entityType;  //  Commercial, Individual, Organization, Internal
    private String status;
    private Boolean isDocAttached;
    //added for business info
    private String companyName;
    private String contactPersonEmail;
    private String contactPersonPhone;
    private String website;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @OneToMany(mappedBy = "entity", cascade = CascadeType.MERGE)
    private List<Contract> contracts;
    @OneToMany(mappedBy = "entity", cascade = CascadeType.MERGE)
    private List<DocuLibrary> docuLibraries;
    @OneToMany(mappedBy = "entity", cascade = CascadeType.MERGE)
    private List<UserLevelPrivilege> userLevelPrivileges;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
