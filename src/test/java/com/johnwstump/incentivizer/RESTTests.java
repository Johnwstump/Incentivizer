package com.johnwstump.incentivizer;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

public abstract class RESTTests {

    public void checkResponseCode(MvcResult result, HttpStatus expectedResponse){
        if (result.getResponse().getStatus() != expectedResponse.value()){
            String error = String.format("Incorrect response code. Received %d and expected %d", result.getResponse().getStatus(), expectedResponse.value());
            Assertions.fail(error);
        }
    }
}
