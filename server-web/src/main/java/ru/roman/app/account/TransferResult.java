package ru.roman.app.account;

/**
 * Money transfer result
 */
public enum TransferResult {

    SUCCESS,
    DEBIT_ACCOUNT_NOT_FOUND,
    CREDIT_ACCOUNT_NOT_FOUND,
    CAN_NOT_TRANSFER_TO_SAME_ACCOUNT,
    AMOUNT_LESS_OR_EQUAL_ZERO,
    INSUFFICIENT_FUNDS,

}
