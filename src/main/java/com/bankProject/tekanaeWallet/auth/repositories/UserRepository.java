package com.bankProject.tekanaeWallet.auth.repositories;

import com.bankProject.tekanaeWallet.auth.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
