package com.be.klash.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRate {
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}
