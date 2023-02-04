package ir.maktab.finalprojectphase2.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
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
public class Offer {

    @EmbeddedId
    private OfferId offerId;

    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    LocalDate createDate;

    @Column(nullable = false)
    Double price;

    @Temporal(TemporalType.DATE)
    LocalDate workDate;

    Duration duration;

    @Column(columnDefinition = "boolean default false")
    boolean confirmedByCustomer;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
