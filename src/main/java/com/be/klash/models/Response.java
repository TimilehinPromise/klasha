/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.be.klash.models;


public enum Response {
    
    SUCCESS("00","Successful"),
    INVALID_CURRENCY("INVALID_CURRENCY","Invalid currency code entered"),
    ACCOUNT_NUMBER_EMPTY("ACCOUNT_NUMBER_EMPTY","Account number or code is empty"),
    HTTP_ERROR("HTTP_ERROR","Error occured while connecting to upstream system"),
    PAYMENT_ERROR("PAYMENT_ERROR","Error occured while processing payment "),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND","Requested resource not found"),
    VALIDATION_ERROR("VALIDATION_ERROR","Request Validation failed"),
    CLIENT_ALREADY_EXIST("CLIENT_ALREADY_EXIST","Sent client already exist"),
    CLIENT_NOT_FOUND("CLIENT_NOT_FOUND","Sent client does not exist "),
    TRANSACTION_ERROR("TRANSACTION_ERROR","Error occured while processing transactions"),
    BAD_MESSAGE_ERROR("BAD_MESSAGE_ERROR","Request sent cannot be converted"),
    DATA_ACCESS_ERROR("DATA_ACCESS_ERROR","Inconsitent data sent "),
    INVALID_TRANSACTION_STATE("INVALID_TRANSACTION_STATE","Transaction in unexpected state "),
    REQUEST_METHOD_ERROR("REQUEST_METHOD_ERROR","Request not in the expected HTTP format"),
    REQUEST_ERROR("REQUEST_ERROR","Request not in the expected format"),
    SYSTEM_ERROR("SYSTEM_ERROR","System Error"),
    AUTHORIZATION_ERROR("AUTHORIZATION_ERROR","Unauthorised access to resource");  
    
    
    
    private Response(String responseCode , String responseMessage ){
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
    }
    private String responseCode;
    private String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    
    
}
