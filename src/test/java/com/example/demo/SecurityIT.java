package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SecurityIT {

    private TestRestTemplate restTemplate;
    private URL base;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate("user", "password");
        base = new URL("http://localhost:" + port);
    }

    @Test
    void whenLoggedUserRequestsHomePage_ThenSuccess() throws IllegalStateException, IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String body = response.getBody();
        assertTrue(body.contains("welcome"));
    }

    @Test
    void whenUserWithWrongCredentials_thenUnauthorizedPage() throws Exception {
        restTemplate = new TestRestTemplate("user", "wrongpassword");
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        final String body = response.getBody();
        assertTrue(body.contains("Unauthorized"));
    }

}
