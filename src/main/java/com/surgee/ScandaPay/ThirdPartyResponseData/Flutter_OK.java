package com.surgee.ScandaPay.ThirdPartyResponseData;

import com.surgee.ScandaPay.model.FlutterUserDetails;

import lombok.Data;


@Data
public class Flutter_OK {
    private String status;
    private String message;
    private FlutterUserDetails data;
}
