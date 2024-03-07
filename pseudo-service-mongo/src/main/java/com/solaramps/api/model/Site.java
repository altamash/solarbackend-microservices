package com.solaramps.api.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

//@Getter
//@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class Site {
    @JsonProperty("siteId")
    @JsonAlias({"id"})
    public Long id;
    @JsonProperty("locations")
    @JsonAlias({ "physicalLocationDTOs"})
    public ArrayList<physicalLocationDTO> physicalLocationDTOs;

}
