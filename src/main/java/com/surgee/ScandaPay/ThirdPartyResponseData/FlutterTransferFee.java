package com.surgee.ScandaPay.ThirdPartyResponseData;

import com.surgee.ScandaPay.data.FlutterFeeObject;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FlutterTransferFee {
    private String status;
    private String message;
    private FlutterFeeObject data;

}
