package pecunia_22.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "currencies")
public class Currency {
    @SequenceGenerator(
            name = "currencies_sequence",
            sequenceName = "currencies_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "currencies_sequence"
    )
    private Long id;
    private String cod;
//    private String pattern;       Usunieta Kolumna
    private String currency;
    private String change;
//    private Integer active;       Usunięta kolumna
    @Column(name = "data_exchange")
    private String dataExchange;
    @Column(name = "currency_series")
    private String currencySeries;
    @Column(name = "currency_from")
    private String currencyFrom;
    private String converter;
    @Column(length = 1024)
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country countries;

    @ManyToOne
    @JoinColumn(name = "active_id")
    private Active actives;

    @ManyToOne
    @JoinColumn(name = "pattern_id")
    private Pattern patterns;

    @OneToMany(mappedBy = "currencies", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Note> notes;

    @OneToMany(mappedBy = "currencies", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Coin> coins;

    @OneToMany(mappedBy = "currencies", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Security> securities;

    @OneToMany(mappedBy = "currencies", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Medal> medals;
}
