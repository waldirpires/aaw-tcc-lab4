package br.newtonpaiva.lab.tcc.integration;

import org.junit.jupiter.api.Test;

public class HelloResourceTest extends AbstractResourceTest{

    @Test
    public void testHelloResource() {
        createRequest().when()
            .get("/v1/hello").then().assertThat().statusCode(200);
    }
}
