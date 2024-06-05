package com.surgee.ScandaPay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlutterUserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String response_code;
    private String response_message;
    private String flw_ref;
    private String order_ref;
    private String account_number;
    private Integer frequency;
    private String bank_name;
    private String created_at;
    private String expiry_date;
    private String note;
    private String amount;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
