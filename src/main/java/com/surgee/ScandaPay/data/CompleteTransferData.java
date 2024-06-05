package com.surgee.ScandaPay.data;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CompleteTransferData {
    private String sourceAccountNumber;
    private String sourceSenderName;
    private String destinationAccountNumber;
    private String destinationReceiverName;
    private String destinationBankName;
    private float amount;
    private float fee;
    private float total;

}
