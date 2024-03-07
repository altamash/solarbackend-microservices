package com.solaramps.api.commons.tenant.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_notification_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowNotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "wf_exec_process_id", referencedColumnName = "id")
    private WorkflowExecProcess workflowExecProcess;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipient_id", referencedColumnName = "acctId")
    private User recipient;
    private String destInfo; // Email Id etc
    private String destType; // Web, Email, Mobile
    @Column(length = 1)
    private String commType; // n or e
    @Column(length = 45)
    private String refCode;
    private Long refId;
    @Column(length = 5000)
    private String message;
    private String status;
    private String errorLog;
    private String toCSV; // Multiple recipient
    private String ccCSV;
    private String bccCSV;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
