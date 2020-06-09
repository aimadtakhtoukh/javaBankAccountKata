package bankaccount.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static bankaccount.model.BankingOperationType.*;

class BankingOperationTest {
    Clock clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault());

    @Test
    @DisplayName("If a banking operation is a withdrawal, getValue() should return a negative value.")
    void withdrawalGetValue() {
        var amount = BigDecimal.ONE;
        var operation = BankingOperation.builder().type(WITHDRAWAL).amount(amount).timestamp(clock.instant()).build();
        assertEquals(amount.negate(), operation.getValue());
    }

    @Test
    @DisplayName("If a banking operation is a deposit, getValue() should return a positive value.")
    void depositGetValue() {
        var amount = BigDecimal.ONE;
        var operation = BankingOperation.builder().type(DEPOSIT).amount(amount).timestamp(clock.instant()).build();
        assertEquals(amount, operation.getValue());
    }
}