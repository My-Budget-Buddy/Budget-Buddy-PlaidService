package com.budgetbuddy.plaid.plaid.services;

import com.plaid.client.model.CountryCode;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.Products;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.request.PlaidApi;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.Arrays;

@Service
public class PlaidService {

    private final PlaidApi plaidClient;

    public PlaidService(PlaidApi plaidClient) {
        this.plaidClient = plaidClient;
    }
    // This is the method to create a link token, used to initialize the Plaid link flow
    public LinkTokenCreateResponse createLinkToken(String userId) throws Exception {
        // Create a user object with the client user ID set to the user ID
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
            .clientUserId(userId);

        // Build the request object with the user object and other required parameters such as the client name, products, country codes, and language
        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
            .user(user)
            .clientName("Budget Buddy")
            .products(Arrays.asList(Products.AUTH, Products.TRANSACTIONS))
            .countryCodes(Arrays.asList(CountryCode.US, CountryCode.CA))
            .language("en");

        // Execute the request to create the link token
        Response<LinkTokenCreateResponse> response = plaidClient
                .linkTokenCreate(request)
                .execute();

        // If the response is successful, return the link token
        if (response.isSuccessful()) {
            return response.body();
        } else {
            // If the response is not successful, throw an exception
            throw new Exception("Error creating link token: " + response.errorBody().string());
        }
    }
    // This is the method to exchange a public token for an access token
    public String exchangePublicToken(String publicToken) throws Exception {
        // Build the request object with the public token with the public token
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
            .publicToken(publicToken);

        // Execute the request to exchange the public token for an access token
        Response<ItemPublicTokenExchangeResponse> response = plaidClient
            .itemPublicTokenExchange(request)
            .execute();

        // If the response is successful, return the access token
        if (response.isSuccessful()) {
            return response.body().getAccessToken();
        } else {
            // If the response is not successful, throw an exception
            throw new Exception("Error exchanging public token: " + response.errorBody().string());
        }
    }

}