package ru.roman.app.account;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ZERO;
import static ru.roman.app.account.TransferResult.AMOUNT_LESS_OR_EQUAL_ZERO;
import static ru.roman.app.account.TransferResult.CAN_NOT_TRANSFER_TO_SAME_ACCOUNT;
import static ru.roman.app.account.TransferResult.CREDIT_ACCOUNT_NOT_FOUND;
import static ru.roman.app.account.TransferResult.DEBIT_ACCOUNT_NOT_FOUND;
import static ru.roman.app.account.TransferResult.INSUFFICIENT_FUNDS;
import static ru.roman.app.account.TransferResult.SUCCESS;

/**
 * Accounts balance management service,
 * preserves account balances in memory
 */
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public TransferResult transferAmount(
            String debitAccountId, String creditAccountId, BigDecimal amount) {

        Preconditions.checkNotNull(debitAccountId, "Debit account cannot be null");
        Preconditions.checkNotNull(creditAccountId, "Credit account cannot be null");
        Preconditions.checkNotNull(amount, "Amount cannot be null");

        Account debitAccount = getAccount(debitAccountId);
        if (debitAccount == null) {
            return DEBIT_ACCOUNT_NOT_FOUND;
        }

        Account creditAccount = getAccount(creditAccountId);
        if (creditAccount == null) {
            return CREDIT_ACCOUNT_NOT_FOUND;
        }

        if (debitAccount.equals(creditAccount)) {
            return CAN_NOT_TRANSFER_TO_SAME_ACCOUNT;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return AMOUNT_LESS_OR_EQUAL_ZERO;
        }

        return lockAndTransfer(debitAccount, creditAccount, amount);
    }

    private Account getAccount(String accountId) {
        return accounts.getOrDefault(accountId, null);
    }

    private TransferResult lockAndTransfer(
            Account debitAccount, Account creditAccount, BigDecimal amount) {

        final Account first;
        final Account second;
        if (debitAccount.getId().compareTo(creditAccount.getId()) > 0) {
            first = debitAccount;
            second = creditAccount;
        } else {
            first = creditAccount;
            second = debitAccount;
        }

        synchronized (first) {
            synchronized (second) {
                return transfer(debitAccount, creditAccount, amount);
            }
        }
    }

    private TransferResult transfer(Account debitAccount, Account creditAccount, BigDecimal amount) {
        BigDecimal debitBalance = debitAccount.getBalance().subtract(amount);

        if (debitBalance.compareTo(ZERO) < 0) {
            return INSUFFICIENT_FUNDS;
        }
        debitAccount.setBalance(debitBalance);
        creditAccount.setBalance(creditAccount.getBalance().add(amount));

        return SUCCESS;
    }
}
