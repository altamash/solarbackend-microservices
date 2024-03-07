package com.solaramps.api.tenant.model.docu;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "docu_modules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocuModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String moduleName;
    @Column(length = 15)
    private String moduleCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
