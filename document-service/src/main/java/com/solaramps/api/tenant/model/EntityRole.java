package com.solaramps.api.tenant.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "entity_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "entity_id")
    private com.solaramps.api.tenant.model.Entity entity;
    @Column(columnDefinition = "false", nullable = false)
    private Boolean isDeleted;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "functional_role_id")
    private FunctionalRoles functionalRoles;

//    @OneToMany(mappedBy = "entityRole")
//    private List<EntityGroup> entityGroups;


    private boolean status;
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String updatedBy;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
