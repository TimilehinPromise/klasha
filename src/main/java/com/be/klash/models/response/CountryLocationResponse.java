package com.be.klash.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryLocationResponse {
    private boolean error;
    private String msg;
    private CountryLocationData data;
}
