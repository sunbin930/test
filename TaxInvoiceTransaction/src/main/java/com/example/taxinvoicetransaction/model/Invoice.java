package com.example.taxinvoicetransaction.model;

import lombok.Data;

@Data
public class Invoice {
    private String customerId;
    private String invoiceNumber;
    private String timeStamp;
    private String amount;
    private String taxType;
}
