package com.solaramps.api.commons.module.queue.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record {
    private Long key;
    private String value;
    private int partition;
    private long offset;

    @Override
    public String toString() {
        return "Record{" +
                "key=" + key +
                ", value='" + value + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                '}';
    }
}
