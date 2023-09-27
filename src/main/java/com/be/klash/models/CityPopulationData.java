package com.be.klash.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityPopulationData {
    private boolean error;
    private String msg;
    private List<CityData> data;

}
