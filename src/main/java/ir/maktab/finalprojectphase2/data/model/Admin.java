package ir.maktab.finalprojectphase2.data.model;

import ir.maktab.finalprojectphase2.data.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    String fullName;

    @Column(unique = true)
    String username;

    String password;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
