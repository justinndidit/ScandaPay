package com.surgee.ScandaPay.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.surgee.ScandaPay.ThirdPartyResponseData.Flutter_OK;
import com.surgee.ScandaPay.requests.TransferMoneyRequest;
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


    public ResponseEntity<?> transferToAccount(String account_bank, 
                                               String account_number,
                                               float amount,
                                               String narration,
                                               String currency,
                                               String reference,
                                               String callback_url,
                                               String debit_currency
                                               ) {

        TransferMoneyRequest requestData =  TransferMoneyRequest
                                            .builder()
                                            .account_bank(account_bank)
                                            .account_number(account_number)
                                            .amount(amount)
                                            .currency(debit_currency)
                                            .reference(reference)
                                            .callback_url(callback_url)
                                            .debit_currency(debit_currency)
                                            .build();
        
        Flutter_OK flutterResponse = webClient.post()
                                                .uri("/transfers")
                                                .bodyValue(requestData)
                                                .retrieve()
                                                .bodyToMono(Flutter_OK.class)
                                                .block();
        System.out.println(flutterResponse);
            return null;
    }

    public ResponseEntity<?> reviewTransferToAccount(String account_name,
                                                        String account_bank, 
                                                        String account_number,
                                                        String bank_name,
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
                                                .destinationBankName("Bank of America")
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
                    return new ResponseEntity<>(response, status);            }
            return null;

        } catch (Exception e) {
            return null;
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
            System.out.println(e.getMessage());
            return FlutterTransferFee.builder().status("error").message("An error occured while trying to get transaction fee").build();
        }
    }

}
