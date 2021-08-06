package com.sample.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInputs {

    //optional fields
    @JsonAnySetter
    private Map<String, Object> optionalFields = new LinkedHashMap<String, Object>();

}
