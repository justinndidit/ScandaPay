package com.surgee.ScandaPay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlutterTransferDetails {
    @Id
    private Long id;
    private String response_code;
    private String response_message;
    private String flw_ref;
    private String order_ref;
    private String account_number;
    private String currency;
    private String debit_currency;
    private float fee;
    private String bank_name;
    private String bank_code;
    private String full_name;
    private String created_at;
    private String expiry_date;
    private String note;
    private float amount;
    private String status;
    private UUID reference;
    private String meta;
    private String narration;
    private String complete_message;
    private int requires_approval;
    private int is_approved;


    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

}
