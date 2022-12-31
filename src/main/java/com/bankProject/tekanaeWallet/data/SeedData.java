package com.bankProject.tekanaeWallet.data;


import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.models.RoleModel;
import com.bankProject.tekanaeWallet.models.UserModel;
import com.bankProject.tekanaeWallet.repositories.RoleRepository;
import com.bankProject.tekanaeWallet.repositories.UserRepository;
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

        RoleModel roleUser = seedRole("USER");
        RoleModel roleVendor = seedRole("VENDOR");
        RoleModel roleAdmin = seedRole("ADMIN");
        UserModel user1 = seedUser(
                "John",
                "Doe",
                "john@gmail.com",
                "#Password123",
                "0787857046",
                List.of(roleUser)
        );

        UserModel user2 = seedUser(
                "Sam",
                "Patrick",
                "patrick@gmail.com",
                "#Password123",
                "07878570346",
                List.of(roleUser, roleVendor)
        );

        UserModel user3 = seedUser(
                "Jane",
                "Angel",
                "angel@gmail.com",
                "#Password123",
                "0787383734",
                List.of(roleUser, roleVendor, roleAdmin)
        );
        userRepository.save(user2);
    }


    public UserModel seedUser(String firstName, String lastName, String email, String password, String contact, List<RoleModel> roles) {
        return userRepository.save(
                UserModel.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .contactNumber(contact)
                        .roles(roles)
                        .build()
        );
    }

    public RoleModel seedRole(String name) {
        return roleRepository.save(
                RoleModel.builder()
                        .name(name)
                        .build()
        );
    }
}

