package com.solaramps.loginservice.saas.model.permission.navigation;

import com.solaramps.loginservice.saas.model.permission.component.ComponentLibrary;
import lombok.*;

import javax.persistence.*;

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
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "comp_id", referencedColumnName = "id")
    private ComponentLibrary componentLibrary;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "nav_id", referencedColumnName = "id")
    private NavigationElement navigationElement;
}
