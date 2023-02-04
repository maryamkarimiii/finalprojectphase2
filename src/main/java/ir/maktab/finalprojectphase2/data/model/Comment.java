package ir.maktab.finalprojectphase2.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Expert expert;

    @ManyToOne
    Customer customer;

    Integer score;

    String customerComment;
}
