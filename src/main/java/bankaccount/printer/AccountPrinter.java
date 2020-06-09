package bankaccount.printer;

import bankaccount.model.Account;
import bankaccount.model.BankingOperation;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class AccountPrinter {

    public static final String HEADER = " Deposits | Withdrawals |  Date  ";
    public static final String BALANCE_LABEL = "Balance : ";

    private final PrintStream printStream;

    public AccountPrinter(PrintStream printStream) {
        if (printStream == null) { throw new IllegalArgumentException("Printer can't be null."); }
        this.printStream = printStream;
    }

    public void printAccount(Account account) {
        printStream.println(HEADER);
        var bankingOperations = new ArrayList<>(account.getBankingOperations());
        bankingOperations.sort(Comparator.comparing(BankingOperation::getTimestamp));
        bankingOperations.forEach(this::printOperation);
        printStream.println(BALANCE_LABEL + account.getBalance());
    }

    private String padForPrinting(String s) {
        return StringUtils.rightPad(s, 12, " ");
    }

    private void printOperation(BankingOperation operation) {
        var result = new StringBuilder();
        var amount = operation.getValue();
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            result.append(padForPrinting(amount.toString()));
            result.append(padForPrinting(""));
        } else {
            result.append(padForPrinting(""));
            result.append(padForPrinting(amount.toString()));
        }
        result.append(padForPrinting(formatTimestamp(operation.getTimestamp())));
        printStream.println(result.toString());
    }

    private String formatTimestamp(Instant timestamp) {
        return LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
