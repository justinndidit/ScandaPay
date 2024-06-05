package com.surgee.ScandaPay.ThirdPartyResponseData;

import lombok.Data;

import com.surgee.ScandaPay.model.FlutterTransferDetails;
    
    
@Data
public class FlutterTransfer_OK {
   
    private String status;
    private String message;
    private FlutterTransferDetails data;

}
