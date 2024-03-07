package com.solaramps.api.tenant.model.docu;

import com.solaramps.api.tenant.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@jakarta.persistence.Entity
@Table(name = "docu_volumes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocuVolume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String volumeName;

/*    @ManyToMany(targetEntity = DocuType.class)
    @JoinTable(name = "docu_volume_docu_type",
            joinColumns = @JoinColumn(name = "docu_volume_id"),
            inverseJoinColumns = @JoinColumn(name = "docu_type_id"))
    private Set<DocuType> docuTypes;*/
//    docuTypeId;
    private Long codeRefId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private DocuModule docuModule;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "docu_vol_map_id")
    @ManyToMany(mappedBy = "docuVolumes")
    private Set<DocuVolMap> docuVolMaps;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
