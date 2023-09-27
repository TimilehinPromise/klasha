package com.be.klash.service;

import com.be.klash.models.payload.ConversionRequest;
import com.be.klash.models.payload.GetCountryRequest;
import com.be.klash.models.payload.GetTopPopulationRequest;
import com.be.klash.models.response.CityPopulationResponse;
import com.be.klash.models.response.ConversionResponse;
import com.be.klash.models.response.CountryDetailsResponse;
import com.be.klash.models.response.StateResponse;

import java.math.BigDecimal;

public interface CountriesService {
    CityPopulationResponse getTopCitiesPopulationCache(GetTopPopulationRequest request);

    CityPopulationResponse getTopCitiesPopulation(GetTopPopulationRequest request);

    CountryDetailsResponse getCountryDataCache(GetCountryRequest request);

    CountryDetailsResponse getCountryData (GetCountryRequest request);

    StateResponse getStateAndLocationCache(GetCountryRequest request);

    StateResponse getStateAndLocation(GetCountryRequest request);

    ConversionResponse getConvertCurrencyCache(ConversionRequest request);

    ConversionResponse convertCurrency (ConversionRequest request);
}
