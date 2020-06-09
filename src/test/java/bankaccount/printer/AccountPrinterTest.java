package bankaccount.printer;

import bankaccount.exceptions.OverdrawnException;
import bankaccount.model.Account;
import bankaccount.model.BankingOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static bankaccount.model.BankingOperationType.DEPOSIT;
import static bankaccount.model.BankingOperationType.WITHDRAWAL;
import static bankaccount.printer.AccountPrinter.BALANCE_LABEL;
import static bankaccount.printer.AccountPrinter.HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountPrinterTest {

    @Test
    @DisplayName("A printer should print empty data for a new account")
    void printEmptyAccount() {
        var baos = new ByteArrayOutputStream();
        var printStream = new PrintStream(baos, true, StandardCharsets.UTF_8);

        var account = new Account.Builder().build();
        new AccountPrinter(printStream).printAccount(account);

        var data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
            HEADER + System.lineSeparator() +
            BALANCE_LABEL + "0" + System.lineSeparator(),
            data
        );
    }

    @Test
    @DisplayName("A printer should show deposits and withdrawals")
    void printAccountWithDepositsAndWithdrawals() throws OverdrawnException {
        var baos = new ByteArrayOutputStream();
        var printStream = new PrintStream(baos, true, StandardCharsets.UTF_8);

        var account = new Account.Builder()
                .addOperation(BankingOperation.builder().type(DEPOSIT).amount(BigDecimal.valueOf(200)).timestamp(Instant.parse("2020-06-09T00:00:00.00Z")).build())
                .addOperation(BankingOperation.builder().type(WITHDRAWAL).amount(BigDecimal.valueOf(100.50)).timestamp(Instant.parse("2020-06-09T03:00:00.00Z")).build())
                .build();
        new AccountPrinter(printStream).printAccount(account);

        var data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
                HEADER + System.lineSeparator() +
                        "200                     09-06-2020  " + System.lineSeparator() +
                        "            -100.5      09-06-2020  " + System.lineSeparator() +
                        BALANCE_LABEL + "99.5" + System.lineSeparator(),
                data
        );
    }

    @Test
    @DisplayName("A printer should show deposits sorted by date")
    void printAccountWithDepositsSortedByDate() throws OverdrawnException {
        var baos = new ByteArrayOutputStream();
        var printStream = new PrintStream(baos, true, StandardCharsets.UTF_8);

        var account = new Account.Builder()
                .addOperation(BankingOperation.builder().type(DEPOSIT).amount(BigDecimal.valueOf(150)).timestamp(Instant.parse("2020-06-09T00:00:00.00Z")).build())
                .addOperation(BankingOperation.builder().type(DEPOSIT).amount(BigDecimal.valueOf(50)).timestamp(Instant.parse("2020-06-06T00:00:00.00Z")).build())
                .build();
        new AccountPrinter(printStream).printAccount(account);

        var data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
                HEADER + System.lineSeparator() +
                        "50                      06-06-2020  " + System.lineSeparator() +
                        "150                     09-06-2020  " + System.lineSeparator() +
                        BALANCE_LABEL + "200" + System.lineSeparator(),
                data
        );
    }


}