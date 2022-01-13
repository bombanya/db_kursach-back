package com.bombanya.kursach.gp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GPFetcher {

    @Value("${space-track.login}")
    private String login;
    @Value("${space-track.password}")
    private String password;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public GPData fetch(int noradId) {
        String url = "https://www.space-track.org/ajaxauth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("identity", login);
        map.add("password", password);
        map.add("query", "https://www.space-track.org//basicspacedata/query/class/gp/NORAD_CAT_ID/" + noradId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        if (response.getBody() == null || response.getBody().length() <= 2) return null;

        try {
            return mapper.readValue(response.getBody().substring(1, response.getBody().length() - 1), GPData.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
