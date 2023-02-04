package ir.maktab.finalprojectphase2.data.model;

import ir.maktab.finalprojectphase2.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String trackingNumber;

    @ManyToOne
    Customer customer;

    @ManyToOne
    SubService subService;

    @ManyToOne
    Expert expert;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    LocalDate orderDate;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    LocalDate workDate;

    @Column(nullable = false)
    String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus orderStatus;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
