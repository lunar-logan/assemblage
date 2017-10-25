package com.anurag.aggregator.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterServiceResponse extends ServiceResponse {
    private static final long serialVersionUID = -3024687568743832264L;
}
