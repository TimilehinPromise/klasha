package com.be.klash.models.response;

import com.be.klash.models.StateCitiesResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponse {
    private List<StateCitiesResponse> citiesResponseList;
}
