package com.bankProject.tekanaeWallet.role.repository;

import com.bankProject.tekanaeWallet.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
