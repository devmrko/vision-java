package com.example.fn;

import org.junit.Rule;
import org.junit.Test;

import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;
import com.oracle.fn.VisionFunction;

public class VisionFunctionTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void shouldReturnGreeting() {
        testing.givenEvent().enqueue();
        testing.thenRun(VisionFunction.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
//        assertEquals("Hello, world!", result.getBodyAsString());
    }

}