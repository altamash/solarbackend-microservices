package com.solaramps.loginservice.tenant.model.contract;

import com.solaramps.loginservice.tenant.model.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@javax.persistence.Entity
@Table(name = "user_level_privilege")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelPrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", nullable = true)
    private Account account;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "contract_id", nullable = true)
    private Contract contract;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "entity_id", nullable = true)
    private Entity entity;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
