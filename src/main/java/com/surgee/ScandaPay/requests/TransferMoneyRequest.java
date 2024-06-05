package com.surgee.ScandaPay.requests;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TransferMoneyRequest {
    String account_name;
    String bank_name;
    String account_bank;
    String account_number;
    float amount;
    String narration;
    String currency;
    String reference;
    String callback_url;
    String debit_currency;
    String source_account_number;
    String source_sender_name;
}
