package com.be.klash.service;

import com.be.klash.controller.ClientAPI;
import com.be.klash.countries.HttpClient;
import com.be.klash.models.*;
import com.be.klash.models.CountryPopulationData;
import com.be.klash.models.payload.ConversionRequest;
import com.be.klash.models.payload.GetCountryRequest;
import com.be.klash.models.payload.GetTopPopulationRequest;
import com.be.klash.models.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import exception.ResourceNotFoundException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountriesServiceImpl implements CountriesService {

    @Autowired
    private  HttpClient httpClient;

    @Autowired
    private ApiRequest apiRequest;


    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    private List<CurrencyConversionRate> conversionRates;
    private Set<String> validCurrencies = new HashSet<>();


    public CountriesServiceImpl() {
        try {
            conversionRates = parseCsvFile("src/main/resources/exchange_rate.csv");
            validCurrencies = createValidCurrenciesSet();
        } catch (IOException e) {
            // Handle the exception
        }
    }

    public LoadingCache<GetTopPopulationRequest, CityPopulationResponse> topCitiesCache  = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public CityPopulationResponse load(GetTopPopulationRequest key) {
                    return getTopCitiesPopulation(key);
                }
            });


    @Override
    public CityPopulationResponse getTopCitiesPopulationCache(GetTopPopulationRequest request){
        try {
            return topCitiesCache.get(request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CityPopulationResponse getTopCitiesPopulation(GetTopPopulationRequest request){

        CityPopulationData cityPopulationData = new CityPopulationData();

        cityPopulationData = apiRequest.callTopCitiesPopulation(ClientAPI.GET_CITIES_AND_POPULATION, null,  null,  HttpMethod.GET);

        if (cityPopulationData.isError()){
            throw new ResourceNotFoundException(cityPopulationData.getMsg());
        }

        List<CityData> cityData = cityPopulationData.getData();

        List<CityPopulation> filteredCities = cityData.stream()
                .filter(city -> city.getCountry() != null && isCityInCountry(city, "Italy", "New Zealand", "Ghana"))
                .map(city -> {
                    String cityName = city.getCity();
                    String countryName = city.getCountry();
                    String populationValue = city.getPopulationCounts().isEmpty() ? "0" : city.getPopulationCounts().get(0).getValue();
                    return new CityPopulation(cityName,countryName,populationValue);
                })
                .filter(city -> isNumeric(city.getPopulationValue()))
                .sorted(Comparator.comparing(city -> Integer.parseInt(city.getPopulationValue()), Collections.reverseOrder()))
                .limit(request.getN())
                .collect(Collectors.toList());


        if (filteredCities.size() < request.getN()) {

            filteredCities = cityData.stream()
                    .map(city -> {
                        String cityName = city.getCity();
                        String countryName = city.getCountry();
                        String populationValue = city.getPopulationCounts().isEmpty() ? "0" : city.getPopulationCounts().get(0).getValue();
                        return new CityPopulation(cityName,countryName,populationValue);
                    })
                    .filter(city -> isNumeric(city.getPopulationValue()))
                    .sorted(Comparator.comparing(city -> Integer.parseInt(city.getPopulationValue()), Collections.reverseOrder()))
                    .limit(request.getN())
                    .collect(Collectors.toList());

        }

        CityPopulationResponse response = new CityPopulationResponse(filteredCities.size(),filteredCities);
        return response;
    }

    @Override
    public CountryDetailsResponse getCountryDataCache(GetCountryRequest request){
        try {
            return countryDataCache.get(request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }



    public LoadingCache<GetCountryRequest, CountryDetailsResponse> countryDataCache  = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public CountryDetailsResponse load(GetCountryRequest key) {
                    return getCountryData(key);
                }
            });

    @Override
    public CountryDetailsResponse getCountryData(GetCountryRequest request){
        String latestValue = "", lat= "", logVal="", currency="", capital = "", iso2 ="" ,iso3 ="";
        CountryPopulationData data = new CountryPopulationData();

         data = apiRequest.callGetCountryPopulation(ClientAPI.GET_COUNTRY_POPULATION,request,null,HttpMethod.POST);
         if (data.isError()){
             throw new ResourceNotFoundException(data.getMsg());
         }

        CityData cityData = data.getData();
        if (cityData.getPopulationCounts() != null && !cityData.getPopulationCounts().isEmpty()) {
            PopulationCount latestPopulationCount = cityData.getPopulationCounts().get(cityData.getPopulationCounts().size() - 1);
             latestValue = latestPopulationCount.getValue();
        }

        CountryLocationResponse locationResponse = apiRequest.callCountryLocation(ClientAPI.GET_COUNTRY_LOCATION,request,null,HttpMethod.POST);
        System.out.println(locationResponse);
        if (data.isError()){
            throw new ResourceNotFoundException(data.getMsg());
        }

        if (!locationResponse.isError() && locationResponse.getData()!=null && !locationResponse.getData().getLog().isEmpty() && !locationResponse.getData().getLat().isEmpty()){
         lat = locationResponse.getData().getLat();
         logVal = locationResponse.getData().getLog();
        }

        currency = getCountryCurrency(request);


        CountryLocationResponse capitalResponse = apiRequest.callCountryLocation(ClientAPI.GET_COUNTRY_CAPITAL,request,null,HttpMethod.POST);
        if (!capitalResponse.isError() && capitalResponse.getData()!=null && !capitalResponse.getData().getCapital().isEmpty()){
            capital =  capitalResponse.getData().getCapital();
            System.out.println(capital);
        }

        CountryLocationResponse codeResponse = apiRequest.callCountryLocation(ClientAPI.GET_COUNTRY_CODES,request,null,HttpMethod.POST);
        if (!codeResponse.isError() && codeResponse.getData()!=null && !codeResponse.getData().getIso2().isEmpty() && !codeResponse.getData().getIso3().isEmpty()){
            iso2 =  codeResponse.getData().getIso2();
            iso3 = codeResponse.getData().getIso3();
            System.out.println(iso2);
            System.out.println(iso3);
        }

        return CountryDetailsResponse.builder()
                .country(request.getCountry())
                .capital(capital)
                .currency(currency)
                .log(logVal)
                .lat(lat)
                .iso2(iso2)
                .iso3(iso3)
                .population(latestValue)
                .build();
    }

    private String getCountryCurrency(GetCountryRequest request){
        CountryLocationResponse currencyResponse = apiRequest.callCountryLocation(ClientAPI.GET_COUNTRY_CURRENCY,request,null,HttpMethod.POST);
        if (currencyResponse.isError()){
            throw new ResourceNotFoundException(currencyResponse.getMsg());
        }
        if (!currencyResponse.isError() && currencyResponse.getData()!=null && !currencyResponse.getData().getCurrency().isEmpty()){
            System.out.println(currencyResponse + "currency response");
            return currencyResponse.getData().getCurrency();
        }
        return null;
    }

    @Override
    public StateResponse getStateAndLocationCache(GetCountryRequest request){
        try {
            return stateAndLocationCache.get(request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public LoadingCache<GetCountryRequest, StateResponse> stateAndLocationCache  = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public StateResponse load(GetCountryRequest key) {
                    return getStateAndLocation(key);
                }
            });

    @Override
    public StateResponse getStateAndLocation(GetCountryRequest request){

        StateDetailsResponse response = apiRequest.getCountryStates(ClientAPI.GET_COUNTRY_STATES,request,null,HttpMethod.POST);
        if (response.isError()){
            throw new ResourceNotFoundException(response.getMsg());
        }
        log.info(response.toString());
        List<StateCitiesResponse> citiesResponseList = new ArrayList<>();

        for (State state: response.getData().getStates()) {
            StateCitiesRequest citiesRequest = StateCitiesRequest.builder()
                            .state(state.getName())
                                    .country(request.getCountry())
                                            .build();

            CitiesData citiesData = httpClient.sendRequest(ClientAPI.GET_STATES_CITIES,citiesRequest,CitiesData.class,null,null,HttpMethod.POST);
            StateCitiesResponse stateCitiesResponse = new StateCitiesResponse(state.getName(),citiesData.getData());
            citiesResponseList.add(stateCitiesResponse);
        }
        StateResponse stateResponse = new StateResponse(citiesResponseList);
        return stateResponse;
    }


    private List<CurrencyConversionRate> parseCsvFile(String fileName) throws IOException {
        List<CurrencyConversionRate> conversionRates = new ArrayList<>();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String sourceCurrency = csvRecord.get("sourceCurrency");
                String targetCurrency = csvRecord.get("targetCurrency");
                double rate = Double.parseDouble(csvRecord.get("rate"));

                CurrencyConversionRate conversionRate = new CurrencyConversionRate(sourceCurrency, targetCurrency, BigDecimal.valueOf(rate));
                conversionRates.add(conversionRate);
            }
        }

        return conversionRates;
    }

    @Override
    public ConversionResponse getConvertCurrencyCache(ConversionRequest request){
        try {
            return convertCurrencyCache.get(request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public LoadingCache<ConversionRequest, ConversionResponse> convertCurrencyCache  = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public ConversionResponse load(ConversionRequest key) {
                    return convertCurrency(key);
                }
            });


    @Override
    public ConversionResponse convertCurrency(ConversionRequest request){
        String sourceCurrency = getCurrency(request.getCountry());

        if (validateTargetCurrency(request.getTargetCurrency())){
            throw new ValidationException("Target Currency Is Not Currently Available");
        }
        if (validateTargetCurrency(sourceCurrency)){
            throw new ValidationException("Source Currency Is Not Currently Available");
        }

        BigDecimal convertedAmount = convertCurrency(sourceCurrency,request.getTargetCurrency(),request.getAmount());

       return ConversionResponse.builder()
               .currency(sourceCurrency)
               .amount(request.getTargetCurrency().concat(String.valueOf(convertedAmount)))
               .build();

    }
    private Set<String> createValidCurrenciesSet() {

        validCurrencies.add("EUR");
        validCurrencies.add("NGN");
        validCurrencies.add("USD");
        validCurrencies.add("JPY");
        validCurrencies.add("GBP");
        validCurrencies.add("UGX");
        return validCurrencies;
    }

    private String getCurrency(String country){
        GetCountryRequest request = new GetCountryRequest(country);
       return getCountryCurrency(request);
    }


    public BigDecimal convertCurrency(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        CurrencyConversionRate rate = findConversionRate(sourceCurrency, targetCurrency);
        log.info(rate.getRate().toString());
        log.info(amount.toString());
        if (rate != null) {

            BigDecimal convertedAmount = amount.multiply(rate.getRate());
            System.out.println(convertedAmount);
            return convertedAmount;
        }

        return null;
    }

    private CurrencyConversionRate findConversionRate(String sourceCurrency, String targetCurrency) {
        System.out.println(conversionRates);
        System.out.println(sourceCurrency);
        for (CurrencyConversionRate rate : conversionRates) {
            if (rate.getSourceCurrency().equals(sourceCurrency) && rate.getTargetCurrency().equals(targetCurrency)) {
                return rate;
            }
            if (rate.getSourceCurrency().equals(targetCurrency) && rate.getTargetCurrency().equals(sourceCurrency)) {
                BigDecimal rateValue = BigDecimal.ONE.divide(rate.getRate(),3, RoundingMode.HALF_UP);
                return new CurrencyConversionRate(targetCurrency, sourceCurrency, rateValue);
            }
        }
        return null;
    }

    private boolean validateTargetCurrency(String currency) {
        if (!validCurrencies.contains(currency))
            return true;
        return false;
    }



    private boolean isCityInCountry(CityData city, String... countries) {
        return Arrays.asList(countries).contains(city.getCountry());
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
