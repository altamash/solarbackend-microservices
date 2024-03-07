package com.solaramps.api.commons.module.queue.alert.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileAttachment {

    private String path;
    private String name;

    @Override
    public String toString() {
        return "FileAttachment{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
