package bankaccount.model;

import bankaccount.exceptions.OverdrawnException;
import bankaccount.repositories.ArrayListBankingOperationRepository;
import bankaccount.repositories.BankingOperationRepository;

import java.math.BigDecimal;
import java.util.List;

public class Account {

    private final BankingOperationRepository repository;

    private Account(BankingOperationRepository repository) {
        this.repository = repository;
    }

    public List<BankingOperation> getBankingOperations() {
        return repository.getOperations();
    }

    public BigDecimal getBalance() {
        return repository.getOperations().stream().map(BankingOperation::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isOverdrawing(BankingOperation operation) {
        if (operation.getType() == BankingOperationType.WITHDRAWAL) {
            return getBalance().add(operation.getValue()).compareTo(BigDecimal.ZERO) < 0;
        }
        return false;
    }

    public static class Builder {
        private final BankingOperationRepository repository;
        private final Account account;

        public Builder() {
            this.repository = new ArrayListBankingOperationRepository();
            this.account = new Account(repository);
        }

        public Builder(BankingOperationRepository repository) {
            this.repository = repository;
            this.account = new Account(repository);
        }

        public Builder addOperation(BankingOperation operation) throws OverdrawnException {
            if (operation == null) { throw new IllegalArgumentException("Operation can't be null."); }
            if (account.isOverdrawing(operation)) {
                throw new OverdrawnException("Trying to take " + operation.getValue() + " when balance is " + account.getBalance());
            }
            repository.add(operation);
            return this;
        }

        public Account build() { return account; }

    }


}
