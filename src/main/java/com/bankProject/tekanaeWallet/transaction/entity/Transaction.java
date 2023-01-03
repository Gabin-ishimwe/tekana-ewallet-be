package com.bankProject.tekanaeWallet.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Column(
            name = "from_account_number"
    )
    private UUID fromAccountNumber;

    @Column(
            name = "to_account_number"
    )
    private UUID toAccountNumber;

    @Column(
            name = "transaction_time"
    )
    private Date transactionTime = new Date(System.currentTimeMillis());
}
