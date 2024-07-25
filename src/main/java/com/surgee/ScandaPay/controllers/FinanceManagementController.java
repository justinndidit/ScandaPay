package com.surgee.ScandaPay.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surgee.ScandaPay.services.FinanceManagementService;
import com.surgee.ScandaPay.requests.TransferMoneyRequest;
import com.surgee.ScandaPay.util.AccountNumberGenerator;
import com.surgee.ScandaPay.DTO.HttpResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FinanceManagementController {

    private final FinanceManagementService financeManagementService;
    private final AccountNumberGenerator accountNumberGenerator;
    @PostMapping("/transfer")
    public ResponseEntity<?> transferToAccount() {
        return null;
    }
    @PostMapping("/transfer/review")
    public ResponseEntity<HttpResponseDTO> reviewTransferDetails(@RequestBody TransferMoneyRequest request) {
        String callback_url = "http://127.0.0.1:8000/api/v1/callbacks/transfer";
        String reference = generateReference();
        return financeManagementService.reviewTransferToAccount(
            request.getAccount_name(),
            request.getAccount_bank(),
            request.getBank_name(),
            request.getAccount_number(),
            request.getAmount(),
            request.getNarration(),
            request.getCurrency(),
            reference,
            callback_url,
            request.getDebit_currency(),
            request.getSource_account_number(),
            request.getSource_sender_name()
            );
    }
    // @GetMapping("/transfers/rates")
    // public ResponseEntity<?> getTransactionFee(@RequestParam Float amount,@RequestParam String destination_currency, @RequestParam String source_currency) {    
    //     return financeManagementService.getTransactionFee(amount, destination_currency, source_currency);
    // }

    

    private String generateReference() {
        return accountNumberGenerator.generateTransferReference();
    }
}
