package com.solaramps.api.tenant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sites")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String siteName;
    private String siteType;
    private String subType;
    private String active;
    private String refCode;
    @Column(name ="ref_id")
    private Long refId;
    /*    @JsonIgnore
        @OneToMany(mappedBy = "site", cascade = CascadeType.MERGE)
        private List<SiteLocation> siteLocations;*/
    @Transient
    private List<LocationMapping> locationMappings;
    @Transient
    private List<PhysicalLocation> physicalLocations;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "ref_id")
//    private Organization organization;
}
