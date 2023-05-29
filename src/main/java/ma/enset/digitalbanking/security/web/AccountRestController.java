package ma.enset.digitalbanking.security.web;

import lombok.AllArgsConstructor;

import ma.enset.digitalbanking.security.entities.AppUser;
import ma.enset.digitalbanking.security.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountRestController {
    AccountService accountService;

    @GetMapping("/")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }
}
