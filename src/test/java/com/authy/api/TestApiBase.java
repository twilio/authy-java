package com.authy.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

public class TestApiBase {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    protected final String testHost = "http://localhost:18089";
    protected final String testApiKey = "test_api_key";
}
