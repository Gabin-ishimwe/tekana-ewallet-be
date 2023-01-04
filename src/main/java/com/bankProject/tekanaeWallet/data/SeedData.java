package com.bankProject.tekanaeWallet.data;


import com.bankProject.tekanaeWallet.account.entity.Account;
import com.bankProject.tekanaeWallet.account.repositories.AccountRepository;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.role.entity.Role;
import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsersAndRole();
    }

    public void seedUsersAndRole() throws NotFoundException {

        Role roleUser = seedRole("USER");
        Role roleAdmin = seedRole("ADMIN");
        Account account1 = seedAccount(5000L);
        User user1 = seedUser(
                "John",
                "Doe",
                "john@gmail.com",
                "#Password123",
                "0787857046",
                List.of(roleUser),
                account1
        );
        Account account2 = seedAccount(10000L);
        User user2 = seedUser(
                "Sam",
                "Patrick",
                "patrick@gmail.com",
                "#Password123",
                "07878570346",
                List.of(roleUser),
                account2
        );
        Account account3 = seedAccount(15000L);
        User user3 = seedUser(
                "Jane",
                "Angel",
                "angel@gmail.com",
                "#Password567",
                "0787383734",
                List.of(roleUser, roleAdmin),
                account3
        );
        userRepository.save(user2);
    }


    public User seedUser(String firstName, String lastName, String email, String password, String contact, List<Role> roles, Account account
    ) {
        return userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .contactNumber(contact)
                        .roles(roles)
                        .account(account)
                        .build()
        );
    }

    public Role seedRole(String name) {
        return roleRepository.save(
                Role.builder()
                        .name(name)
                        .build()
        );
    }

    public Account seedAccount(Long amount) {
        return accountRepository.save(
                Account.builder()
                        .account_number(UUID.randomUUID())
                        .balance(amount)
                        .build()
        );
    }
}

