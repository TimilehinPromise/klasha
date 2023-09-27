package com.be.klash.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDetailsResponse {
    private String country;
    @JsonProperty(value = "log")
    private String log;
    private String lat;
    private String currency;
    private String capital;
    private String iso2;
    private String iso3;
    private String population;
}
