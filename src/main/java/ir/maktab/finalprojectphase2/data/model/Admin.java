package ir.maktab.finalprojectphase2.data.model;

import ir.maktab.finalprojectphase2.data.enums.Role;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
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
