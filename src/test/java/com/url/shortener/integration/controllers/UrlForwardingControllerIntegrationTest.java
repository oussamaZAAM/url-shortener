package com.url.shortener.integration.controllers;

import com.url.shortener.config.WebSecurityConfig;
import com.url.shortener.controllers.UrlForwardingController;
import com.url.shortener.services.UrlExpanderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(UrlForwardingController.class)
@Import(WebSecurityConfig.class)
public class UrlForwardingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlExpanderService urlExpanderService;

    @Test
    void testExpandUrl_SuccessfulExpansion() throws Exception {
        // Arrange
        String shortUrl = "AwmPiR8gzuA";
        String originalUrl = "http://www.example.com";
        when(urlExpanderService.expandShortUrl(any(String.class))).thenReturn(originalUrl);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{shortUrl}", shortUrl))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl(originalUrl));
    }

    @Test
    void testExpandUrl_ShortUrlNull() throws Exception {
        // Arrange
        String shortUrl = null;

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{shortUrl}", shortUrl).param("shortUrl", shortUrl))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testExpandUrl_ShortUrlEmpty() throws Exception {
        // Arrange
        String shortUrl = "ㅤ";

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{shortUrl}", shortUrl))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testExpandUrl_UrlNotFound() throws Exception {
        // Arrange
        String shortUrl = "NonExistentShortUrl";
        when(urlExpanderService.expandShortUrl(any(String.class))).thenReturn(null);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{shortUrl}", shortUrl))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Short URL not found"));
    }
}
