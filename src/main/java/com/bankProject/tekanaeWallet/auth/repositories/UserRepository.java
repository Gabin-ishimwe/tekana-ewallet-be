package com.bankProject.tekanaeWallet.auth.repositories;

import com.bankProject.tekanaeWallet.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
