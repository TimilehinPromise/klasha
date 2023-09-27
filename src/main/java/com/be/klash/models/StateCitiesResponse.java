package com.be.klash.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateCitiesResponse {
    private String state;
    private List<String> cities;
}
