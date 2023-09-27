package com.be.klash.models.response;

import com.be.klash.models.CityPopulation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityPopulationResponse {
    private int size;
    List<CityPopulation> populationList;
}
