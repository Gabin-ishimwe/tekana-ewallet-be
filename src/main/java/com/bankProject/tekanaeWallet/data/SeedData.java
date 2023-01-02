package com.bankProject.tekanaeWallet.data;


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

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsersAndRole();
    }

    public void seedUsersAndRole() throws NotFoundException {

        Role roleUser = seedRole("USER");
        Role roleVendor = seedRole("VENDOR");
        Role roleAdmin = seedRole("ADMIN");
        User user1 = seedUser(
                "John",
                "Doe",
                "john@gmail.com",
                "#Password123",
                "0787857046",
                List.of(roleUser)
        );

        User user2 = seedUser(
                "Sam",
                "Patrick",
                "patrick@gmail.com",
                "#Password123",
                "07878570346",
                List.of(roleUser, roleVendor)
        );

        User user3 = seedUser(
                "Jane",
                "Angel",
                "angel@gmail.com",
                "#Password123",
                "0787383734",
                List.of(roleUser, roleVendor, roleAdmin)
        );
        userRepository.save(user2);
    }


    public User seedUser(String firstName, String lastName, String email, String password, String contact, List<Role> roles) {
        return userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .contactNumber(contact)
                        .roles(roles)
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
}

