package com.solaramps.api.tenant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_type_ref_map")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeTypeRefMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10)
    private String regCode;
    private String refCode;
    private Long regModuleId;
    private String type;
    private String refTable;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
