package com.bankProject.tekanaeWallet.role.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_role_name",
                        columnNames = "role_name"
                )
        }
)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(
            message = "Role name is required"
    )
    @Column(
            name = "role_name"
    )
    private String name;

//    @ManyToMany
//    @JoinTable(
//            name = "role_user_mapping",
//            joinColumns = @JoinColumn(
//                    name = "role_id",
//                    referencedColumnName = "id"
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "user_id",
//                    referencedColumnName = "id"
//            )
//    )
//    private List<User> user;
}
