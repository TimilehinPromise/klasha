package com.be.klash.controller;

import com.be.klash.models.payload.ConversionRequest;
import com.be.klash.models.payload.GetCountryRequest;
import com.be.klash.models.payload.GetTopPopulationRequest;
import com.be.klash.models.response.CityPopulationResponse;
import com.be.klash.models.response.ConversionResponse;
import com.be.klash.models.response.CountryDetailsResponse;
import com.be.klash.models.response.StateResponse;
import com.be.klash.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/country")
public class CountriesController {

    @Autowired
    private CountriesService countriesService;

    @GetMapping("")
    public ResponseEntity<CityPopulationResponse> getCitiesPopulation(@ModelAttribute GetTopPopulationRequest request) {
        CityPopulationResponse response = countriesService.getTopCitiesPopulationCache(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/country-data")
    public ResponseEntity<CountryDetailsResponse> getCitiesPopulation(@RequestBody GetCountryRequest request) {
        CountryDetailsResponse response = countriesService.getCountryDataCache(request);
       return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/state-data")
    public ResponseEntity<StateResponse> getCountryStateCities(@RequestBody GetCountryRequest request) {
        StateResponse response = countriesService.getStateAndLocationCache(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping("/conversion")
    public ResponseEntity<ConversionResponse> getCountryStateCities(@RequestBody ConversionRequest request) {
        ConversionResponse response = countriesService.getConvertCurrencyCache(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
