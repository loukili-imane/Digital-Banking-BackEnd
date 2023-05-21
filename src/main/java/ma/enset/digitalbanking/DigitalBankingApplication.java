package ma.enset.digitalbanking;

import ma.enset.digitalbanking.dtos.BankAccountDTO;
import ma.enset.digitalbanking.dtos.CurrentBankAccountDTO;
import ma.enset.digitalbanking.dtos.SavingBankAccountDTO;
import ma.enset.digitalbanking.entities.CurrentAccount;
import ma.enset.digitalbanking.entities.Customer;
import ma.enset.digitalbanking.entities.Operation;
import ma.enset.digitalbanking.entities.SavingAccount;
import ma.enset.digitalbanking.enums.AccountStatus;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;
import ma.enset.digitalbanking.dtos.CustomerDTO;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import ma.enset.digitalbanking.repositories.OperationRepository;
import ma.enset.digitalbanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}
/*
	@Bean
			CommandLineRunner start(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Hassan","Imane","Mohamed").forEach(name->{
				CustomerDTO customer=new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount:bankAccounts){
				for (int i = 0; i <10 ; i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAccountDTO){
						accountId=((SavingBankAccountDTO) bankAccount).getId();
					} else{
						accountId=((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
				}
			}
		};
	}

 */
	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
							OperationRepository operationRepository){
		return  args -> {
				Stream.of("Imane","hala","rachida").forEach(name->{
					Customer customer = new Customer();
					customer.setName(name);
					customer.setEmail(name+"@gmail.com");
					customerRepository.save(customer);
				});
				customerRepository.findAll().forEach(cust -> {
					CurrentAccount currentAccount = new CurrentAccount();
					currentAccount.setBalance(Math.random()*5000);
					currentAccount.setCreatedAt(new Date());
					currentAccount.setCustomer(cust);
					currentAccount.setStatus(AccountStatus.Created);
					currentAccount.setOverDraft(5000);
					currentAccount.setId(UUID.randomUUID().toString());
					bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
					savingAccount.setId(UUID.randomUUID().toString());
					savingAccount.setBalance(Math.random()*5000);
				savingAccount.setStatus(AccountStatus.Created);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});
				bankAccountRepository.findAll().forEach(acc->{
					for(int i=0;i<10;i++){
						Operation operation = new Operation();
						operation.setOperationDate(new Date());
						operation.setAmount(Math.random()*12000);
						operation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
						operation.setBankAccount(acc);
						operationRepository.save(operation);
					}
				});
		};
	}
}
