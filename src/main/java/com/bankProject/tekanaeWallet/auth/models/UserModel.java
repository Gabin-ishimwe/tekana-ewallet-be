package com.bankProject.tekanaeWallet.auth.models;

import com.bankProject.tekanaeWallet.role.models.RoleModel;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue
    private Long id;
    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "last_name"
    )
    private String lastName;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "password"
    )
    private String password;

    @Column(
            name = "contact_number"
    )
    private String contactNumber;
    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_role_mapping",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private List<RoleModel> roles;
}
