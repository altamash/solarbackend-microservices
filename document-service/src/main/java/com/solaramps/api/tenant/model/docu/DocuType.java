package com.solaramps.api.tenant.model.docu;

import com.solaramps.api.tenant.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "docu_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocuType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String docuType;

    /*@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "volume_id", referencedColumnName = "volumeId")*/

//    @ManyToMany(mappedBy = "docuTypes")
//    private Set<DocuVolume> volumes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Transient
    private DocuVolMap docuVolMap;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
