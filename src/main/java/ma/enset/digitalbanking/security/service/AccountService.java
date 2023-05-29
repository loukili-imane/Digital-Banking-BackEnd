package ma.enset.digitalbanking.security.service;

import ma.enset.digitalbanking.security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
