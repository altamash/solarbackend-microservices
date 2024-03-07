package com.solaramps.api.tenant.model.docu;

import com.solaramps.api.tenant.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "docu_vol_map")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocuVolMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "docu_type_PAV_id")
    private Long docuTypePAVId;
    @Column(name = "vol_PAV_id")
    private Long volPAVId;

//    @MapsId
    @OneToOne
    @JoinColumn(name = "docu_type_id", unique = true)
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "docu_type_id")
    private DocuType docuType;
//    @MapsId
//    @OneToMany(mappedBy = "docuVolMap", cascade = CascadeType.MERGE)
//    @JoinColumn(name = "docu_volume_id")
//    @OneToMany(mappedBy = "docuVolMap", cascade = CascadeType.MERGE)
    @ManyToMany(targetEntity = DocuVolume.class)
    @JoinTable(name = "docu_vol_map_docu_volumes",
            joinColumns = @JoinColumn(name = "docu_vol_map_id"),
            inverseJoinColumns = @JoinColumn(name = "docu_volumes_id"))
    private Set<DocuVolume> docuVolumes;

    private Boolean isMandatory;
    private Integer noFilesAllowed;
    private Integer maxFileSize;
    @ElementCollection
    private List<String> allowedFileType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @OneToMany(mappedBy = "docuVolMap", cascade = CascadeType.MERGE)
    private Set<DocuLibrary> docuLibraries;

//    @OneToOne(mappedBy = "docuVolMap")
//    private DocuLibrary docuLibrary;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    public void setDocuVolumes(List<DocuVolume> docuVolumes) {
//        this.docuVolumes.clear();
//        this.docuVolumes.addAll(docuVolumes);
//    }
//
//    public void removeDocuVolume(DocuVolume docuVolume) {
//        this.docuVolumes.remove(docuVolume);
//    }
}
