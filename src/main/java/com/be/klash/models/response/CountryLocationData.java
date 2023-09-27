package com.be.klash.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryLocationData {
    private String name;
    @JsonProperty(value = "Iso2")
    private String iso2;
    @JsonProperty(value = "Iso3")
    private String iso3;
    private String currency;
    private String capital;
    @JsonProperty(value = "long")
    private String log;
    private String lat;

}
