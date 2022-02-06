package com.example.taxinvoicetransaction.service;

public interface TaxService {
    String readCommand();

    boolean checkValidCommand(String command);

    String report(String command);
}
