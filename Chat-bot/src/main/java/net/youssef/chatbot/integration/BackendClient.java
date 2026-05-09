package net.youssef.chatbot.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class BackendClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BackendClient(RestTemplateBuilder builder,
                         @Value("${backend.api.base-url:http://localhost:8085}") String baseUrl) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
    }

    public List<BankAccountInfo> listAccounts() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/accounts")
                .build()
                .toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BankAccountInfo>>() {
                }).getBody();
    }

    public BankAccountInfo getAccount(String accountId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/accounts/{accountId}")
                .buildAndExpand(accountId)
                .toUri();
        return restTemplate.getForObject(uri, BankAccountInfo.class);
    }

    public AccountHistoryInfo getAccountHistory(String accountId, int page, int size) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/accounts/{accountId}/pageOperations")
                .queryParam("page", page)
                .queryParam("size", size)
                .buildAndExpand(accountId)
                .toUri();
        return restTemplate.getForObject(uri, AccountHistoryInfo.class);
    }

    public boolean backendAvailable() {
        try {
            restTemplate.getForObject(baseUrl + "/accounts", String.class);
            return true;
        } catch (RestClientException ex) {
            return false;
        }
    }

    public List<CustomerInfo> listCustomers() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/customers")
                .build()
                .toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CustomerInfo>>() {
                }).getBody();
    }
}
