package ru.roman.app.account;

import java.math.BigDecimal;

/**
 * Money transferring service
 */
public interface MoneyTransferService {

    TransferResult transferAmount(String debitAccount, String creditAccount, BigDecimal amount);

}
