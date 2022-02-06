package com.example.taxinvoicetransaction.controller;

import com.example.taxinvoicetransaction.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxController {

    @Autowired
    private TaxService taxService;

    @GetMapping("/TIT")
    public String taxInvoiceTransaction() {
        System.out.println("---------- Tax Invoice Transaction ----------");
        System.out.println("Enter \"Q\" to quit!");
        System.out.println("Please input the command (\"TIT taxType customerId filename.csv\"):");

        while(true){
            String command = taxService.readCommand();
            if(command.equalsIgnoreCase("Q")){
                break;
            }
            if(taxService.checkValidCommand(command)){
                System.out.println(taxService.report(command));
            }else{
                System.out.println("Please input the valid command!");
            }
            System.out.println("Please input the next command:");
        }

        System.out.println("Thank you for using, Bye!");
        return "Thank you for using, Bye!";
    }
}
