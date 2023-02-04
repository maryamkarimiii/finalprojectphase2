package ir.maktab.finalprojectphase2.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    Double baseAmount;

    @Column(nullable = false)
    String description;

    @ManyToOne
    Service service;

    @ToString.Exclude
    @ManyToMany(mappedBy = "subServiceSet")
    Set<Expert> expertSet;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
