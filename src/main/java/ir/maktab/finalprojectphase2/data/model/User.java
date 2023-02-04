package ir.maktab.finalprojectphase2.data.model;

import ir.maktab.finalprojectphase2.data.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    String firstName;

    String lastName;

    @Column(unique = true, length = 11, nullable = false)
    String phoneNumber;

    @Column(unique = true, nullable = false, updatable = false)
    String email;

    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    LocalDate registrationDate;

    @OneToOne(cascade = CascadeType.ALL)
    Wallet wallet;

    @Column(unique = true, nullable = false)
    String username;

    @Column(length = 8, nullable = false)
    String password;
}
