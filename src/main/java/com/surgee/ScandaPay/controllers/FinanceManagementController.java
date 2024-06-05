package com.surgee.ScandaPay.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.surgee.ScandaPay.services.FinanceManagementService;
import com.surgee.ScandaPay.requests.TransferMoneyRequest;
import com.surgee.ScandaPay.util.AccountNumberGenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FinanceManagementController {

    private final FinanceManagementService financeManagementService;
    private final AccountNumberGenerator accountNumberGenerator;
    @PostMapping("/transfer")
    public ResponseEntity<?> transferToAccount(@RequestBody TransferMoneyRequest request) {
        String callback_url = "http://127.0.0.1:8000/api/v1/callbacks/transfer";
        String reference = accountNumberGenerator.generateTransferReference();

        financeManagementService.transferToAccount(
            request.getAccount_bank(),
            request.getAccount_number(),
            request.getAmount(),
            request.getNarration(),
            request.getCurrency(),
            callback_url,
            reference,
            request.getDebit_currency()
            );
        return null;
    }
    @PostMapping("/transfer/review")
    public ResponseEntity<?> reviewTransferDetails(@RequestBody TransferMoneyRequest request) {
        String callback_url = "http://127.0.0.1:8000/api/v1/callbacks/transfer";
        String reference = accountNumberGenerator.generateTransferReference();
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

    @PostMapping("callbacks/transfer")
    public ResponseEntity<?> transferCallback() {
        System.out.print("Call back hit");
        return null;
    }
}
