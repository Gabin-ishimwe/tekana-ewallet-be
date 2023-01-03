package com.bankProject.tekanaeWallet.transaction.repository;

import com.bankProject.tekanaeWallet.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
