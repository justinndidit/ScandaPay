package com.surgee.ScandaPay.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.web.reactive.function.client.WebClient;

import com.surgee.ScandaPay.ThirdPartyResponseData.Flutter_OK;
//import com.surgee.ScandaPay.requests.TransferMoneyRequest;
import com.surgee.ScandaPay.DTO.HttpResponseDTO;
import com.surgee.ScandaPay.ThirdPartyResponseData.FlutterTransferFee;
import com.surgee.ScandaPay.data.CompleteTransferData;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FinanceManagementService {
        private final WebClient webClient;


    public ResponseEntity<?> transferToAccount() {
      
        Flutter_OK flutterResponse = webClient.post()
                                                .uri("/transfers")
                                                .bodyValue("gh")
                                                .retrieve()
                                                .bodyToMono(Flutter_OK.class)
                                                .block();
        System.out.println(flutterResponse);
        return null;
    }

    public ResponseEntity<HttpResponseDTO> reviewTransferToAccount(String account_name,
                                                        String account_bank, 
                                                        String bank_name,
                                                        String account_number,
                                                        float amount,
                                                        String narration,
                                                        String currency,
                                                        String reference,
                                                        String callback_url,
                                                        String debit_currency,
                                                        String source_account_number,
                                                        String source_sender_name
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int statusCode = 500;
        boolean success = false;
        try {

            FlutterTransferFee feeResponse =  getTransactionFee(amount, debit_currency, currency);
            if (feeResponse != null && feeResponse.getStatus().equals("success")){
                    status = HttpStatus.OK;
                    success = true;
                    statusCode = 200;
                    float fee = feeResponse.getData().getFee();
                    CompleteTransferData transferData = CompleteTransferData.builder()
                                                .sourceAccountNumber(account_bank)
                                                .sourceSenderName(source_sender_name)
                                                .destinationAccountNumber(account_number)
                                                .destinationReceiverName(account_name)
                                                .destinationBankName(bank_name)
                                                .amount(amount)
                                                .fee(fee)
                                                .total(amount + fee)
                                                .build();
                   HttpResponseDTO response = HttpResponseDTO
                                              .builder()
                                              .data(transferData)
                                              .message("Transfer details reviewed successfully")
                                              .status(status)
                                              .statusCode(statusCode)
                                              .error(null)
                                              .success(success)
                                              .build();
                    return new ResponseEntity<>(response, status);   
                }

            throw new IllegalTransactionStateException("Something Went wrongn while fetching transfer fee");

        } catch (Exception e) {
            return new ResponseEntity<> (HttpResponseDTO
            .builder()
            .data(null)
            .error(e.getMessage())
            .message(e.getMessage())
            .success(success)
            .status(status)
            .statusCode(statusCode)
            .stackTrace(e.getCause() != null ? e.getCause().toString() : null)
            .build(), status);
        }
    }
    public FlutterTransferFee getTransactionFee(Float amount, String destination_currency, String source_currency) {
        try {
            FlutterTransferFee flutterResponse = webClient.get()
                    .uri("transfers/rates?amount="+ amount + "&destination_currency="+ destination_currency +"&source_currency=" + source_currency)
                    .retrieve()
                    .bodyToMono(FlutterTransferFee.class)
                    .block();
            if(flutterResponse != null){
                return flutterResponse;
            }
            throw new Exception("An error occured while trying to get transaction fee");

        }catch (Exception e) {
            return FlutterTransferFee.builder().status("error").message("An error occured while trying to get transaction fee").build();
        }
    }

}
