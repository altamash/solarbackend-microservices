package com.solaramps.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class physicalLocationDTO {
        @JsonProperty("physical_loc_Id")
        @JsonAlias({"id"})
        public Long  id;
        @JsonProperty("enabled")
        public boolean enabled;
}

