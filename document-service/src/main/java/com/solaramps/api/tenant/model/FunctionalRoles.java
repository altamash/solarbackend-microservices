package com.solaramps.api.tenant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "functional_roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionalRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Long defaultPrivilegeLevel;
    private String defaultHierarchyId; //keeping sequence no 001.001
    private String defaultHierarchySeqCode; //keeping sequence no 001.001
    private String category;
    private String subCategory;
    private String hierarchyType; //project,partner,
    private String status; //project,partner,

    @OneToMany(mappedBy = "functionalRoles")
    private List<EntityRole> entityRoles;
    @OneToMany(mappedBy = "functionalRoles")
    private List<DefaultUserGroup> defaultUserGroups;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
