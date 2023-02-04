package ir.maktab.finalprojectphase2.data.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class OfferId implements Serializable {
    @ManyToOne
    private Order order;
    @ManyToOne
    private Expert expert;
}
