package com.solaramps.api.commons.module.queue.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.solaramps.api.commons.module.queue.alert.mapper.FileAttachment;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowNotificationLogDTO {
    private Long id;
    private Long workflowExecProcessId;
    private Long recipientId;
    private String destInfo; // Email Id etc
    private String destType; // Web, Email, Mobile
    private String commType; // n or e
    private String refCode;
    private Long refId;
//    private String message;
    private String status;
    private String errorLog;
    private String toCSV; // Multiple recipient
    private String ccCSV;
    private String bccCSV;
//    private List<String> tos;
//    private List<String> ccs;
//    private List<String> bccs;
    private String templateId;
    private Map<String, Object> placeholderValues;
    private String propertiesJsonString;
    private String tenantName;
//    private String fileAttachmentPath;
//    private String fileAttachmentName;
    private List<FileAttachment> fileAttachments;
    private String eventName;
    private String brandId;

    @Override
    public String toString() {
        return "WorkflowNotificationLogDTO{" +
                "id=" + id +
                ", workflowExecProcessId=" + workflowExecProcessId +
                ", recipientId=" + recipientId +
                ", destInfo='" + destInfo + '\'' +
                ", destType='" + destType + '\'' +
                ", commType='" + commType + '\'' +
                ", refCode='" + refCode + '\'' +
                ", refId=" + refId +
                ", status='" + status + '\'' +
                ", errorLog='" + errorLog + '\'' +
                ", toCSV='" + toCSV + '\'' +
                ", ccCSV='" + ccCSV + '\'' +
                ", bccCSV='" + bccCSV + '\'' +
                ", templateId='" + templateId + '\'' +
                ", placeholderValues=" + placeholderValues +
                ", propertiesJsonString='" + propertiesJsonString + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", fileAttachments=" + fileAttachments +
                ", eventName='" + eventName + '\'' +
                ", brandId='" + brandId + '\'' +
                '}';
    }
}
