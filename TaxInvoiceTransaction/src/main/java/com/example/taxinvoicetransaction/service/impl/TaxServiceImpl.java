package com.example.taxinvoicetransaction.service.impl;

import com.example.taxinvoicetransaction.model.Invoice;
import com.example.taxinvoicetransaction.model.Report;
import com.example.taxinvoicetransaction.service.TaxService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

@Service
public class TaxServiceImpl implements TaxService {
    public static final BigDecimal TAX_RATE = new BigDecimal("0.1");
    Invoice invoice = new Invoice();
    Report report = new Report();

    @Override
    public String readCommand() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        return command;
    }

    @Override
    public boolean checkValidCommand(String command) {
        return command.matches("^\\s*TIT\\s+(GST|PAYROLL|COMPANY_TAX|LAND_TAX|CAPITOL_GAIN)\\s+\\S+\\s+\\S+\\.csv\\s*$");
    }

    @Override
    public String report(String command) {
        String[] cmd = command.trim().split("\\s+");
        report.setTaxType(cmd[1]);
        report.setCustomerId(cmd[2]);
        report.setTax(taxComputation(cmd[3]));

        return "For tax " + report.getTaxType() + ", customer " + report.getCustomerId() + " has declared $" + report.getTax();
    }

    private String taxComputation(String filename) {
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal tax;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null){
                line = line.replaceAll("\\s+","");
                String[] item = line.split(",");
                if(item.length != 5 || !checkInvoice(item)){
                    System.out.println("!!! Invalid tax invoice: " + line);
                    continue;
                }

                invoice.setCustomerId(item[0]);
                invoice.setInvoiceNumber(item[1]);
                invoice.setTimeStamp(item[2]);
                invoice.setAmount(item[3]);
                invoice.setTaxType(item[4]);

                if(item[0].equals(report.getCustomerId()) && item[4].equals(report.getTaxType())) {
                    totalAmount = totalAmount.add(new BigDecimal(item[3]));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        tax = TAX_RATE.multiply(totalAmount);
        return tax.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private boolean checkInvoice(String[] item) {
        return (item[0].matches("^\\S+$")
                && item[1].matches("^\\S+$")
                && item[2].matches("^[1-2]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T(2[0-3]|[0-1]\\d):[0-5]\\d:[0-5]\\d$")
                && item[3].matches("^(([1-9]\\d*)|0)(\\.\\d{1,2})?$")
                && item[4].matches("^(GST|PAYROLL|COMPANY_TAX|LAND_TAX|CAPITOL_GAIN)$"));
    }
}
