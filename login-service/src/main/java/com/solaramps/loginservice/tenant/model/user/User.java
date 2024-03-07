package com.solaramps.loginservice.tenant.model.user;

import com.solaramps.loginservice.tenant.model.permission.AvailablePermissionSet;
import com.solaramps.loginservice.tenant.model.permission.PermissionGroup;
import com.solaramps.loginservice.tenant.model.user.role.Role;
import com.solaramps.loginservice.tenant.model.user.userType.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctId;
    @Transient
    private String jwtToken;
    private Long compKey;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    private String IdCode;       //Authority ID
    private String authorityId;  //e.g. SSN, Driving License
    private String gender;
    private Date dataOfBirth;
    private Date registerDate;
    private Date activeDate;
    private String status;
    @Column(length = 1000)
    private String notes;
    private String prospectStatus;
    private String referralEmail;
    private Date deferredContactDate;
    private String language;
    private String authentication;
    private boolean isEmailVerified;
    @ManyToOne
    @JoinColumn(name = "user_type", nullable = false)
    private UserType userType;
    @ManyToMany(targetEntity = Role.class)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @ManyToMany(targetEntity = PermissionGroup.class)
    @JoinTable(name = "user_permission_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_group_id"))
    private Set<PermissionGroup> permissionGroups;
    //    @ElementCollection
    @ManyToMany(targetEntity = AvailablePermissionSet.class)
    @JoinTable(name = "user_permission_set",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_set_id"))
    private Set<AvailablePermissionSet> permissionSets;
//    @ElementCollection
//    private Set<Long> permissionSetIds = new HashSet<>(); // Only ids because PermissionSet are in saas schema
//    @Transient
//    private Set<PermissionSet> permissionSets = new TreeSet<>();

    private String category;
    private String groupId;
    @Lob
    private byte[] photo;
    private String socialUrl;
    private String emailAddress;
    private Boolean ccd;
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "workflow_group_id", referencedColumnName = "id")
//    private WorkflowGroupAssignment workflowGroupAssignment;
    @Transient
    private String externalId;
    @Transient
    private String action;
    @Transient
    private String uploadPassword;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroups.add(permissionGroup);
    }

    public void removePermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroups.remove(permissionGroup);
    }

    public void addPermissionSet(AvailablePermissionSet permissionSet) {
        this.permissionSets.add(permissionSet);
    }

    public void removePermissionSet(AvailablePermissionSet permissionSet) {
        this.permissionSets.remove(permissionSet);
    }

}
