package com.solaramps.api.tenant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_mapping")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long locationId; //physicalLocId
    private String primaryInd;
    private String status;
    /*@ManyToOne
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;*/
    private Long sourceId;
    private String sourceType;
    @Transient
    private Long siteId;
    @Transient
    private PhysicalLocation physicalLocation;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
