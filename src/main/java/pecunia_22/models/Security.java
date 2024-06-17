package pecunia_22.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "securities")
public class Security extends Common{

    public Security(String itemDate, Date dateBuy, String nameCurrency, Double priceBuy, Double priceSell,
                    Integer quantity, String statusSell, String description, String aversPath, String reversePath,
                    Double denomination, String series, Integer width, Integer height, Timestamp created_at, Timestamp updated_at,
                    Boolean visible, String unitCurrency, String unitQuantity, Bought boughts, Quality qualities, Status statuses, Active actives, ImageType imageTypes) {
        super( itemDate, dateBuy, nameCurrency, priceBuy, priceSell, quantity, statusSell, description,
                unitQuantity, visible, unitCurrency, aversPath, reversePath, created_at, updated_at, boughts, qualities, statuses, actives, imageTypes);
        this.denomination = denomination;
        this.series = series;
        this.width = width;
        this.height = height;
    }
    public Security() {
    }

    @SequenceGenerator(
            name = "securities_sequence",
            sequenceName = "securities_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "securities_sequence"
    )
    private Long id;
    @Column(name = "denomination")
    private Double denomination;
    @Column(name = "series")
    private String series;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currencies;

    @ManyToOne
    @JoinColumn(name = "making_id")
    private Making makings;
}
