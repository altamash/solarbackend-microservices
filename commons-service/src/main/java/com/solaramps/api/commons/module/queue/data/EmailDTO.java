package com.solaramps.api.commons.module.queue.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailDTO {
    private String name;
    private String email;
    private String type;
    private String value;
    private String subject;
}
