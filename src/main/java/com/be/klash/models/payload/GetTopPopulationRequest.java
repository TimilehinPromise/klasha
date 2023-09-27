package com.be.klash.models.payload;

import com.be.klash.annotations.IsNumeric;
import lombok.Data;

@Data
public class GetTopPopulationRequest {
    @IsNumeric
    private Long n;
}
