package ma.enset.digitalbanking;

import ma.enset.digitalbanking.entities.CurrentAccount;
import ma.enset.digitalbanking.entities.Customer;
import ma.enset.digitalbanking.entities.Operation;
import ma.enset.digitalbanking.entities.SavingAccount;
import ma.enset.digitalbanking.enums.AccountStatus;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import ma.enset.digitalbanking.repositories.OperationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

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
