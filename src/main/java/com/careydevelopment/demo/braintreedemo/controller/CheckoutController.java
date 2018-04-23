package com.careydevelopment.demo.braintreedemo.controller;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;

@Controller
public class CheckoutController {

    /**
     * This is dependency injected from the BraintreeConfig class
     */
    @Autowired
    private BraintreeGateway gateway;

    /**
     * Successful status codes from credit card processing
     */
    private Status[] TRANSACTION_SUCCESS_STATUSES = new Status[] {
        Transaction.Status.AUTHORIZED,
        Transaction.Status.AUTHORIZING,
        Transaction.Status.SETTLED,
        Transaction.Status.SETTLEMENT_CONFIRMED,
        Transaction.Status.SETTLEMENT_PENDING,
        Transaction.Status.SETTLING,
        Transaction.Status.SUBMITTED_FOR_SETTLEMENT
    };


    /**
     * Main page - accepts a couple of different payment types
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.GET)
    public String checkout(Model model) {
        //get the token
        String clientToken = gateway.clientToken().generate();
        
        //add the token to the model - this will be used in JavaScript code
        model.addAttribute("clientToken", clientToken);
        
        //serve new.html
        return "newTransaction";
    }

    /**
     * We get here when the user submits the transaction - note that it's a POST
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.POST)
    public String postForm(@RequestParam("amount") String amount, 
            @RequestParam("payment_method_nonce") String nonce, Model model, 
            final RedirectAttributes redirectAttributes) {
        
        //get rid of whitespace
        amount = amount.trim();
        
        BigDecimal decimalAmount;
        
        try {
            //get the decimal version of the text entered
            decimalAmount = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            //we get here if it's not a valid number
            String errorMessage = getErrorMessage(amount);
            redirectAttributes.addFlashAttribute("errorDetails", errorMessage);
            redirectAttributes.addFlashAttribute("amount", amount);
            return "redirect:checkouts";
        }

        //submit the request for processing
        TransactionRequest request = new TransactionRequest()
            .amount(decimalAmount)
            .paymentMethodNonce(nonce)
            .options()
                .submitForSettlement(true)
                .done();

        //get the response
        Result<Transaction> result = gateway.transaction().sale(request);
        
        //if it's a successful transaction, go to the transaction results page
        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            return "redirect:checkouts/" + transaction.getId();
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            return "redirect:checkouts/" + transaction.getId();
        } else {
            //if the transaction failed, return to the payment page and display all errors
            String errorString = "";
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
               errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
            }
            redirectAttributes.addFlashAttribute("errorDetails", errorString);
            redirectAttributes.addFlashAttribute("amount", amount);
            return "redirect:checkouts";
        }
    }

    
    /**
     * Creates the server-side error message
     */
    private String getErrorMessage(String amount) {
        String errorMessage = amount + " is not a valid price.";

        if (amount.equals("")) {
            errorMessage = "Please enter a valid price.";
        } 
        
        return errorMessage;
    }
    

    /**
     * We get here when the user has submitted a transaction and received a
     * Transaction ID.
     */
    @RequestMapping(value = "/checkouts/{transactionId}")
    public String getTransaction(@PathVariable String transactionId, Model model) {
        Transaction transaction;
        CreditCard creditCard;
        Customer customer;

        try {
            //find the transaction by its ID
            transaction = gateway.transaction().find(transactionId);
            
            //grab credit card info
            creditCard = transaction.getCreditCard();
            
            //grab the customer info
            customer = transaction.getCustomer();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return "redirect:/checkouts";
        }

        //set a boolean that determines whether or not the transaction was successful
        model.addAttribute("isSuccess", Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus()));
        
        //put the relevant objects in the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("customer", customer);

        //server transactionResults.html
        return "transactionResults";
    }
}
