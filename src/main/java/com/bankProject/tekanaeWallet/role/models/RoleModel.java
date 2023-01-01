package com.bankProject.tekanaeWallet.role.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleModel {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role_name")
    private String name;
}
