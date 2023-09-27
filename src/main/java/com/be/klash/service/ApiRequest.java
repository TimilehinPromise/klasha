package com.be.klash.service;

import com.be.klash.countries.HttpClient;
import com.be.klash.models.APIResponse;
import com.be.klash.models.CityPopulationData;
import com.be.klash.models.CountryPopulationData;
import com.be.klash.models.StateDetailsResponse;
import com.be.klash.models.response.CountryLocationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.TimeZone;

@Slf4j
@Service
public class ApiRequest {

    @Autowired
    private HttpClient httpClient;


    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    public CountryPopulationData callGetCountryPopulation(final String path, final Object request,
                                                           HttpHeaders headers,
                                                           final HttpMethod method){


        log.info(request + " e");
        String response = "";
        try {

            final ResponseEntity<String> responseEntity = httpClient.apiCall(path,request,null ,method);

            response = responseEntity.getBody();
            return OBJECT_MAPPER.readValue(response, CountryPopulationData.class);
        } catch (HttpClientErrorException http) {
            response = http.getResponseBodyAsString();
            try {
                CountryPopulationData countryPopulationData = new CountryPopulationData();
                APIResponse errorResponse = OBJECT_MAPPER.readValue(response, APIResponse.class);
                log.info(errorResponse.toString());
                countryPopulationData.setMsg(errorResponse.getMsg());
                countryPopulationData.setError(errorResponse.isError());

                return  countryPopulationData;
            } catch (Exception jsonex) {
                log.info("exception");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }

    public CityPopulationData callTopCitiesPopulation(final String path, final Object request,
                                                       HttpHeaders headers,
                                                       final HttpMethod method){
        log.info(request + " e");

        String response = "";
        try {
            final ResponseEntity<String> responseEntity = httpClient.apiCall(path,request,null ,method);
            response = responseEntity.getBody();
            return OBJECT_MAPPER.readValue(response, CityPopulationData.class);
        } catch (HttpClientErrorException http) {
            response = http.getResponseBodyAsString();
            try {
                CityPopulationData cityPopulationData = new CityPopulationData();
                APIResponse errorResponse = OBJECT_MAPPER.readValue(response, APIResponse.class);
                log.info(errorResponse.toString());
                cityPopulationData.setMsg(errorResponse.getMsg());
                cityPopulationData.setError(errorResponse.isError());
                return  cityPopulationData;
            } catch (Exception jsonex) {
                log.info("exception");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }

    public CountryLocationResponse callCountryLocation(final String path, final Object request,
                                                      HttpHeaders headers,
                                                      final HttpMethod method){
        log.info(request + " e");

        String response = "";
        try {
            final ResponseEntity<String> responseEntity = httpClient.apiCall(path,request,null ,method);
            response = responseEntity.getBody();
            return OBJECT_MAPPER.readValue(response, CountryLocationResponse.class);
        } catch (HttpClientErrorException http) {
            response = http.getResponseBodyAsString();
            try {
                CountryLocationResponse locationResponse = new CountryLocationResponse();
                APIResponse errorResponse = OBJECT_MAPPER.readValue(response, APIResponse.class);
                log.info(errorResponse.toString());
                locationResponse.setMsg(errorResponse.getMsg());
                locationResponse.setError(errorResponse.isError());
                return  locationResponse;
            } catch (Exception jsonex) {
                log.info("exception");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }


    public StateDetailsResponse getCountryStates(final String path, final Object request,
                                                       HttpHeaders headers,
                                                       final HttpMethod method){
        log.info(request + " e");

        String response = "";
        try {
            final ResponseEntity<String> responseEntity = httpClient.apiCall(path,request,null ,method);
            response = responseEntity.getBody();
            return OBJECT_MAPPER.readValue(response, StateDetailsResponse.class);
        } catch (HttpClientErrorException http) {
            response = http.getResponseBodyAsString();
            try {
                StateDetailsResponse detailsResponse = new StateDetailsResponse();
                APIResponse errorResponse = OBJECT_MAPPER.readValue(response, APIResponse.class);
                log.info(errorResponse.toString());
                detailsResponse.setMsg(errorResponse.getMsg());
                detailsResponse.setError(errorResponse.isError());
                return  detailsResponse;
            } catch (Exception jsonex) {
                log.info("exception");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }

}
