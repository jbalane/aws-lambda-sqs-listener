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
public class SQSRequest {

    //optional fields
    private RequestInputs txnReqInputs;

    //mandatory fields
    @JsonAnySetter
    private Map<String, Object> mandatoryFields = new LinkedHashMap<String, Object>();

}
