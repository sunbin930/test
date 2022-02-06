package com.example.taxinvoicetransaction.model;

import lombok.Data;

@Data
public class Report {
    private String taxType;
    private String customerId;
    private String tax;
}
