package com.solaramps.api.tenant.model;

import com.solaramps.api.tenant.model.docu.DocuLibrary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@jakarta.persistence.Entity
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
    private String organizationType; //master or BusinessUnit
    private Long parentOrgId; //master org id for BusinessUnit
    private String logoImage;
    private Boolean isDocAttached;
    private Boolean primaryIndicator;
    private String status;
    //    @OneToMany(targetEntity = Site.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @OneToMany(mappedBy = "refId", cascade = CascadeType.MERGE)
    private List<Site> sites;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<Entity> entities;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<DocuLibrary> docuLibraries;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.MERGE)
    private List<UserLevelPrivilege> userLevelPrivileges;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Long subType; // business unit type(i.e distribution centre)
    private Long organizationSubType;// business unit type(i.e distribution centre)
}
