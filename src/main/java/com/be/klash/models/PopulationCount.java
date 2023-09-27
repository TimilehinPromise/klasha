package com.be.klash.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopulationCount {
    private String year;
    private String value;
    private String sex;
    private String reliability;
}
