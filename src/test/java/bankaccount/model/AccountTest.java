package bankaccount.model;

import bankaccount.exceptions.OverdrawnException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static bankaccount.model.BankingOperationType.*;

class AccountTest {

    Clock clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault());

    @Test
    @DisplayName("An account should be empty on initialization")
    void anAccountShouldBeEmptyOnInitialization() {
        var account = new Account.Builder().build();
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertTrue(account.getBankingOperations().isEmpty());
    }

    @Test
    @DisplayName("An account should increase its balance when a deposit is made")
    void anAccountShouldIncreaseItsBalanceWhenADepositIsMade() throws OverdrawnException {
        var amount = BigDecimal.ONE;
        var deposit = BankingOperation.builder().type(DEPOSIT).amount(amount).timestamp(clock.instant()).build();
        var account = new Account.Builder().addOperation(deposit).build();
        assertEquals(amount, account.getBalance());
        assertEquals(List.of(deposit), account.getBankingOperations());
    }

    @Test
    @DisplayName("Adding an operation should fail if a withdrawal is over the current balance")
    void addingAnOperationShouldFailIfAWithdrawalIsOverTheCurrentBalance() {
        assertThrows(OverdrawnException.class,
                () -> new Account.Builder().addOperation(BankingOperation.builder().type(WITHDRAWAL).amount(BigDecimal.ONE).timestamp(clock.instant()).build())
        );
    }

    @Test
    @DisplayName("If an empty account gets a deposit and a withdrawal from the same amount it should be empty")
    void ifAnEmptyAccountGetsADepositAndAWithdrawalFromTheSameAmountItShouldBeEmpty() throws OverdrawnException {
        var amount = BigDecimal.ONE;
        var deposit = BankingOperation.builder().type(DEPOSIT).amount(amount).timestamp(clock.instant()).build();
        var withdrawal = BankingOperation.builder().type(WITHDRAWAL).amount(amount).timestamp(clock.instant()).build();
        var account =
            new Account.Builder()
                .addOperation(deposit)
                .addOperation(withdrawal)
                .build();
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(List.of(deposit, withdrawal), account.getBankingOperations());
    }

}