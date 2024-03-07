package com.solaramps.api.commons.saas.model.permission.navigation;

import com.solaramps.api.commons.saas.model.permission.component.ComponentLibrary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nav_comp_map")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavigationComponentMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "nav_map_id")
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "comp_id", referencedColumnName = "id")
    private ComponentLibrary componentLibrary;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "nav_id", referencedColumnName = "id")
    private NavigationElement navigationElement;
}
