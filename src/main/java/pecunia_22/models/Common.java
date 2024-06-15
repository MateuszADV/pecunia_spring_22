package pecunia_22.models;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@MappedSuperclass
public abstract class Common {
    //    private String bought;          //Miejsce Zakupu
    @Column(name = "item_date")
    private String itemDate;
    @Column(name = "date_buy")
    private Date dateBuy;           //Data zakupu/ Data utworzenia seta banknotów
    @Column(name = "name_currency")
    private String nameCurrency;        //Nazwa waluty/ Nazwa Seta
    //    @Column(name = "signature_code")
//    private Integer signatureCode;      // Sygnatura banknotu/ oznaczenie Seta (Opcjonalne narazie - 999 kod doseta określający różne banknoty)
    @Column(name = "price_buy")
    private Double priceBuy;            //Cena zakupu
    @Column(name = "price_sell")
    private Double priceSell;           //Cena Sprzedaży (sugerowana)
    @Column(name = "quantity")
    private Integer quantity;           //ilość
    @Column(name = "unit_quantity")
    private String unitQuantity;       //Określa rodzaj ilości(szt, set...)
    //    @Column(name = "quality")
//    private String quality;             //Stan Itemu
//    @Column(name = "status")
//    private String status;              // Status (kolekcja, na sprzedaż, sprzedane
    @Column(name = "status_sell")
    private String statusSell;          //StatusSell odpowiada za to czy dany banknot został wystawiony na sprzedaż
    @Column(name = "description", length = 4092)
    private String description;         //Opis
    //    @Column(name = "img_type")
//    private String imgType;             //Typ obrazka(skan, foto, ze strony)
    @Column(name = "visible")
    private Boolean visible;            //Określa czy dany element jest widoczny dla wszystkich
    @Column(name = "unit_currency")
    private String unitCurrency;
    @Column(name = "avers_path", length = 4092)
    private String aversPath;
    @Column(name = "reverse_path", length = 4092)
    private String  reversePath;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "bought_id")
    private Bought boughts;

    @ManyToOne
    @JoinColumn(name = "quality_id")
    private Quality qualities;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statuses;

    @ManyToOne
    @JoinColumn(name = "active_id")
    private Active actives;

    @ManyToOne
    @JoinColumn(name = "image_type_id")
    private ImageType imageTypes;
}
