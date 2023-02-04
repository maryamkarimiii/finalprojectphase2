package ir.maktab.finalprojectphase2.data.model;


import ir.maktab.finalprojectphase2.data.enums.ExpertRegistrationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Entity
public class Expert extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ExpertRegistrationStatus expertRegistrationStatus;

    @Column(length = 10000)
    String image;

    Double totalScore;
    @ManyToMany
    @ToString.Exclude
    Set<SubService> subServiceSet;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
