package bankaccount.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class BankingOperation {
    private final BankingOperationType type;
    private final BigDecimal amount;
    private final Instant timestamp;

    private BankingOperation(Builder builder) {
        this.type = builder.type;
        this.amount = builder.amount;
        this.timestamp = builder.timestamp;
    }

    public BankingOperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getValue() {
        if (type == BankingOperationType.WITHDRAWAL) {
            return amount.negate();
        }
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankingOperation operation = (BankingOperation) o;
        return type == operation.type &&
                Objects.equals(amount, operation.amount) &&
                Objects.equals(timestamp, operation.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, timestamp);
    }

    public static Type builder() {
        return new Builder();
    }

    private static class Builder implements Type, Amount, Timestamp, Build {
        private BankingOperationType type;
        private BigDecimal amount;
        private Instant timestamp;

        public Amount type(BankingOperationType type) {
            if (type == null) { throw new IllegalArgumentException("Type can't be null"); }
            this.type = type;
            return this;
        }

        public Timestamp amount(BigDecimal amount) {
            if (amount == null) { throw new IllegalArgumentException("Amount can't be null"); }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount can't be zero or less.");
            }
            this.amount = amount;
            return this;
        }

        public Build timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            if (timestamp == null) { throw new IllegalArgumentException("Timestamp can't be null"); }
            return this;
        }

        public BankingOperation build() {
            return new BankingOperation(this);
        }

    }

    public interface Type {
        Amount type(BankingOperationType type);
    }

    public interface Amount {
        Timestamp amount(BigDecimal amount);
    }

    public interface Timestamp {
        Build timestamp(Instant timestamp);
    }

    public interface Build {
        BankingOperation build();
    }
}
