package bankaccount.repositories;

import bankaccount.model.BankingOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayListBankingOperationRepository implements BankingOperationRepository {

    private final List<BankingOperation> bankingOperations = new ArrayList<>();

    @Override
    public void add(BankingOperation bankingOperation) {
        if (bankingOperation == null) {throw new IllegalArgumentException("Operation can't be null.");}
        bankingOperations.add(bankingOperation);
    }

    @Override
    public List<BankingOperation> getOperations() {
        return Collections.unmodifiableList(bankingOperations);
    }
}
