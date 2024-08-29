package com.budgetbuddy.plaid.plaid.controllers;

import com.budgetbuddy.plaid.plaid.services.PlaidService;
import com.plaid.client.model.LinkTokenCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plaid")
public class PlaidController {

    private final PlaidService plaidService;

    public PlaidController(PlaidService plaidService) {
        this.plaidService = plaidService;
    }

    /**
     * Create a Plaid Link token.
     * The token is used to initialize the Plaid Link flow, allowing the user to connect their bank account.
     *
     * @param userId the user id of the link token is created
     * @return the generated link token
     */
    @PostMapping("/link")
    public ResponseEntity<String> linkAccount(@RequestBody String userId) {
        try {
            LinkTokenCreateResponse response = plaidService.createLinkToken(userId);
            String linkToken = response.getLinkToken();
            return ResponseEntity.ok(linkToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating link token: " + e.getMessage());
        }
    }

    /**
     * Exchange the public token for an access token
     * The token is used to initialize the Plaid Link flow, allowing the user to connect their bank account.
     *
     * @param userId the user id of the link token is created
     * @return the generated link token
     */
    @PostMapping("/exchange")
    public ResponseEntity<String> exchangePublicToken(@RequestBody String publicToken) {
        try {
            String accessToken = plaidService.exchangePublicToken(publicToken);
            return ResponseEntity.ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error exchanging public token: " + e.getMessage());
        }
    }

}
