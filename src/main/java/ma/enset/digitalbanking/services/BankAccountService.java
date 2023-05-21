package ma.enset.digitalbanking.services;

import ma.enset.digitalbanking.entities.BankAccount;
import ma.enset.digitalbanking.entities.CurrentAccount;
import ma.enset.digitalbanking.entities.Customer;
import ma.enset.digitalbanking.entities.SavingAccount;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    SavingAccount saveSavingBankAccount(double initBalance, double interest, Long custID) throws CustomerNotFoundException;
    CurrentAccount saveCurrentBankAccount(double initBalance, double overDraft, Long custID) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountID) throws BankAccountNotFoundException;
    void debit( String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit( String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer( String accountIdSource, double amount, String accountIdDEstination) throws BankAccountNotFoundException, BalanceNotSufficientException;

}
