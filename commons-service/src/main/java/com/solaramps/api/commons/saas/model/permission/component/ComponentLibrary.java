package com.solaramps.api.commons.saas.model.permission.component;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "component_library")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentLibrary implements Comparable<ComponentLibrary> {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String componentName;
    @Column(length = 1000)
    private String description;
    private Integer level;
    private Long parentId;
    private String module;
    private String subModule;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "component_type_provision_id", referencedColumnName = "id")
    private ComponentTypeProvision componentTypeProvision;
    private String compType;
    private String source;

/*    @ManyToMany(mappedBy = "componentLibraries")
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "component_library_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_set_id"))
    private Set<PermissionSet> permissionSets = new HashSet<>();*/

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public int compareTo(ComponentLibrary o) {
        return this.componentName.compareTo(o.getComponentName());
    }
}
