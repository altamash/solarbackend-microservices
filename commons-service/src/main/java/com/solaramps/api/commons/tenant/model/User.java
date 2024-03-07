package com.solaramps.api.commons.tenant.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    @Column(nullable = true, unique = true, columnDefinition = "NONE")
    private String userName;
    @Column(nullable = true)
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
    private String authentication; // standard/ NA / oauth
    private boolean isEmailVerified;
    /*@ManyToOne
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<UserLevelPrivilege> userLevelPrivileges;*/
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
    /*@Column(name = "customer_subscriptions")
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.MERGE)
    private List<CustomerSubscription> customerSubscriptions;
    @Column(name = "addresses")
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.MERGE)
    private Set<Address> addresses;
    @OneToMany(mappedBy = "portalAccount", cascade = CascadeType.MERGE)
    private Set<PaymentInfo> paymentInfos;
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.MERGE)
    private Set<BillingHead> billingHeads;*/
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "workflow_group_id", referencedColumnName = "id")
    private WorkflowGroupAssignment workflowGroupAssignment;
    @Transient
    private String externalId;
    @Transient
    private String action;
    @Transient
    private String uploadPassword;
//    @OneToOne(mappedBy = "user")
//    private Account account;
    private Integer privLevel;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
