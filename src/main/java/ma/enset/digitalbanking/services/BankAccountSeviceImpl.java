package ma.enset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking.entities.*;
import ma.enset.digitalbanking.enums.AccountStatus;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import ma.enset.digitalbanking.repositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor @NoArgsConstructor
@Slf4j
public class BankAccountSeviceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private OperationRepository operationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initBalance, double interest, Long custID) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(custID).orElse(null);
        if( customer==null) throw new CustomerNotFoundException("cusomer not found");
        SavingAccount bankAccount = new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setStatus(AccountStatus.Created);
        bankAccount.setInterestRate(interest);
        SavingAccount savedSavingAccount = bankAccountRepository.save(bankAccount);
        return savedSavingAccount;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initBalance, double overDraft, Long custID) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(custID).orElse(null);
        if( customer==null) throw new CustomerNotFoundException("cusomer not found");
       CurrentAccount bankAccount = new CurrentAccount();
       bankAccount.setId(UUID.randomUUID().toString());
       bankAccount.setBalance(initBalance);
       bankAccount.setCustomer(customer);
       bankAccount.setCreatedAt(new Date());
       bankAccount.setStatus(AccountStatus.Created);
       bankAccount.setOverDraft(overDraft);
       CurrentAccount savedcurrentAccount = bankAccountRepository.save(bankAccount);
        return savedcurrentAccount;
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountID) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountID).orElseThrow(()-> new BankAccountNotFoundException("Account not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
            BankAccount bankAccount = getBankAccount(accountId);
            if(bankAccount.getBalance()<amount)
                throw  new BalanceNotSufficientException("Balance not Sufficient");
        Operation operation = new Operation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
bankAccount.setBalance(bankAccount.getBalance()-amount);
bankAccountRepository.save(bankAccount);
              }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);

        Operation operation = new Operation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, double amount, String accountIdDEstination) throws BankAccountNotFoundException, BalanceNotSufficientException {
                debit(accountIdSource,amount,"transfer to "+accountIdDEstination);
                credit(accountIdDEstination,amount,"transfer from "+accountIdSource);
    }
}
