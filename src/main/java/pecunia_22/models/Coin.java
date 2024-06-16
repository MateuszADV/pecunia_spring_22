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
@Table(name = "coins")
public class Coin extends Common{

    public Coin(String itemDate, Date dateBuy, String nameCurrency, Double priceBuy, Double priceSell,
                Integer quantity, String statusSell, String description, String aversPath, String reversePath,
                Double denomination, String series, String composition, Double diameter, Double thickness, Double weight,
                Timestamp created_at, Timestamp updated_at,
                Boolean visible, String unitCurrency, String unitQuantity, Bought boughts, Quality qualities, Status statuses, Active actives, ImageType imageTypes) {
        super( itemDate, dateBuy, nameCurrency, priceBuy, priceSell, quantity, statusSell, description,
                unitQuantity, visible, unitCurrency, aversPath, reversePath, created_at, updated_at, boughts, qualities, statuses, actives, imageTypes);
        this.denomination = denomination;
        this.composition = composition;
        this.diameter = diameter;
        this.thickness = thickness;
        this.weight = weight;
        this.series = series;
    }
    public Coin() {
    }

    @SequenceGenerator(
            name = "coins_sequence",
            sequenceName = "coins_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coins_sequence"
    )
    private Long id;
    @Column(name = "denomination")
    private Double denomination;
    @Column(name = "series")
    private String series;
    @Column(name = "composition")
    private String composition;
    @Column(name = "diameter")
    private Double diameter;
    @Column(name = "thickness")
    private Double thickness;
    @Column(name = "weight")
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currencies;
}
