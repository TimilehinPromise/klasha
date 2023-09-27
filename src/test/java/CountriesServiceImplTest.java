import com.be.klash.controller.ClientAPI;
import com.be.klash.models.*;
import com.be.klash.models.CountryPopulationData;
import com.be.klash.models.payload.GetCountryRequest;
import com.be.klash.models.payload.GetTopPopulationRequest;
import com.be.klash.models.response.*;
import com.be.klash.service.ApiRequest;
import com.be.klash.service.CountriesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
public class CountriesServiceImplTest {

    @Mock
    private ApiRequest apiRequest;

    @Mock
    private HttpClient httpClient;

    private CountriesServiceImpl countriesService;




    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apiRequest = new ApiRequest();
        httpClient = HttpClient.newHttpClient();
        countriesService = new CountriesServiceImpl();
    }



    @Test
    void testGetTopCitiesPopulation() {
        // Arrange
        ApiRequest apiRequest = mock(ApiRequest.class);
        GetTopPopulationRequest request = new GetTopPopulationRequest();
        request.setN(1L);
        CityData cityData = new CityData();
        cityData.setCity("Kumasi");
        PopulationCount populationCount = new PopulationCount();
        populationCount.setValue("12242");
        populationCount.setYear("2012");
        cityData.setPopulationCounts(List.of(populationCount));
        List<CityData>data = new ArrayList<>();
        data.add(cityData);

        CityPopulationData expectedData = new CityPopulationData();
        expectedData.setMsg("received");
        expectedData.setError(false);
        expectedData.setData(data);
        // Populate with expected data
        when(apiRequest.callTopCitiesPopulation(any(), any(), any(), any())).thenReturn(expectedData);

        CountriesServiceImpl service = new CountriesServiceImpl();
        ReflectionTestUtils.setField(service, "apiRequest", apiRequest);

        // Act
        CityPopulationResponse response = service.getTopCitiesPopulation(request);

        // Assert
        assertEquals(expectedData.getData().size(), response.getPopulationList().size());
    }

    @Test
    void testGetCountryData() {
        // Arrange
        ApiRequest apiRequest = mock(ApiRequest.class);
        GetCountryRequest request = new GetCountryRequest();
        request.setCountry("Nigeria");
        PopulationCount populationCount = new PopulationCount();
        populationCount.setReliability("quite relaible");
        populationCount.setSex("Male");
        populationCount.setYear("2011");
        populationCount.setValue("234353");
        CityData cityData = new CityData();
        cityData.setCountry("Nigeria");
        cityData.setCity("Ojo");
        cityData.setPopulationCounts(List.of(populationCount));
        CountryPopulationData expectedData = new CountryPopulationData();
        expectedData.setError(false);
        expectedData.setMsg("successfully");
        expectedData.setData(cityData);



        when(apiRequest.callGetCountryPopulation(any(), any(), any(), any())).thenReturn(expectedData);

        CountriesServiceImpl service = new CountriesServiceImpl();
        ReflectionTestUtils.setField(service, "apiRequest", apiRequest);

        CountryPopulationData response = apiRequest.callGetCountryPopulation(ClientAPI.GET_COUNTRY_POPULATION,request,null,HttpMethod.POST);

        assertEquals(expectedData.getData().getCountry(), response.getData().getCountry());
    }



}
