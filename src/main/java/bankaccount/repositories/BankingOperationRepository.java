package bankaccount.repositories;

import bankaccount.model.BankingOperation;

import java.util.List;

public interface BankingOperationRepository {

    void add(BankingOperation bankingOperation);

    List<BankingOperation> getOperations();
}
