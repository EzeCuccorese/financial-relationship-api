package com.fintech.relationship.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpringdocConfigTest {

    @Test
    void customOpenAPITest() {
        final SpringdocConfig config = new SpringdocConfig();
        final OpenAPI openAPI = config.customOpenAPI();
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
    }
}
