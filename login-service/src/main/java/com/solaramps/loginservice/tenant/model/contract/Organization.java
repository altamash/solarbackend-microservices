package com.solaramps.loginservice.tenant.model.contract;

import com.solaramps.loginservice.tenant.model.document.DocuLibrary;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@javax.persistence.Entity
@Table(name = "organization")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String organizationName;
    private String businessDescription;
    private String logoImage;
    private Boolean isDocAttached;
    private Boolean primaryIndicator;
    private String status;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<com.solaramps.loginservice.tenant.model.contract.Entity> entities;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<DocuLibrary> docuLibraries;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<UserLevelPrivilege> userLevelPrivileges;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
