package com.be.klash.models.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRequest {

    private String country;
    private String targetCurrency;
    private BigDecimal amount;
}
