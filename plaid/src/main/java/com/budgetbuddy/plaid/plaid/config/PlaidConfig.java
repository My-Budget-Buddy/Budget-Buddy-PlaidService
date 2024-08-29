package com.budgetbuddy.plaid.plaid.config;

import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class PlaidConfig {

    @Value("${plaid.client.id}")
    private String clientId;

    @Value("${plaid.secret}")
    private String secret;

    @Bean
    public PlaidApi plaidClient() {
        // This is a map used to store API keys
        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", clientId);
        apiKeys.put("secret", secret);

        // Create an instance of the API client
        ApiClient apiClient = new ApiClient(apiKeys);
        // Set the Plaid enviroment to the sandbox environment
        apiClient.setPlaidAdapter(ApiClient.Sandbox);
        // Return the Plaid API configured with the Client ID and Secret
        return apiClient.createService(PlaidApi.class);
    }
}