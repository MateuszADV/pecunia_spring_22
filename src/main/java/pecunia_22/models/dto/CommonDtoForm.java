package pecunia_22.models.dto;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pecunia_22.models.dto.ImageType.ImageTypeDtoSelect;
import pecunia_22.models.dto.active.ActiveDtoSelect;
import pecunia_22.models.dto.bought.BoughtDtoSelect;
import pecunia_22.models.dto.quality.QualityDtoSelect;
import pecunia_22.models.dto.status.StatusDtoSelect;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@MappedSuperclass
public abstract class CommonDtoForm {

    //    private String bought;          //Miejsce Zakupu
    @Pattern(regexp="([0-9]{4}|[0-9]{4}.[0-9]{2}|[0-9]{4}.[0-9]{2}.[0-9]{2})", message = "Podaj poprawną datę 2000; 2000.01 ;2000.01.01")
    private String itemDate;
    private Date dateBuy;           //Data  zakupu/ Data utworzenia seta banknotów
    private String nameCurrency;        //Nazwa waluty/ Nazwa Seta
    //    private Integer signatureCode;      // Sygnatura banknotu/ oznaczenie Seta (Opcjonalne narazie - 999 kod doseta określający różne banknoty)
//    @Pattern(regexp = "(^[0-9]*\\.[0-9]{2}$)", message = "To nie jest liczba")
    @Digits(integer=6, fraction = 2, message = "Podaj poprawną cenę (1.23)")
    @DecimalMin(value = "0.00", message = "Wartość nie może być ujemna")
    @NotNull(message = "Wartość nie może być pusta")
    private Double priceBuy;            //Cena zakupu
    @Digits(integer=6, fraction = 2, message = "Podaj poprawną cenę (1.23)")
    @DecimalMin(value = "0.00", message = "Wartość nie może być ujemna")
    @NotNull(message = "Wartość nie może być pusta")
    private Double priceSell;           //Cena Sprzedaży (sugerowana)
    @PositiveOrZero(message = "Wartość >=0 ")
    @NotNull(message = "Wartość nie może być NULL")
    private Integer quantity;           //ilość
    private String unitQuantity;       //Określa rodzaj ilości(szt, set...)
    //    @NotNull
//    @NotEmpty
//    private String quality;             //Stan Itemu
//    private String status;              // Status (kolekcja, na sprzedaż, sprzedane
    private String statusSell;          //StatusSell odpowiada za to czy dany banknot został wystawiony na sprzedaż
    private String description;         //Opis
    //    private String imgType;             //Typ obrazka(skan, foto, ze strony)
    private Boolean visible;            //Określa czy dany element jest widoczny dla wszystkich
    private String unitCurrency;
    private String aversPath;
    private String  reversePath;
    private Timestamp created_at;
    private Timestamp updated_at;

    @NotNull
    @Valid
    private BoughtDtoSelect boughts;

    @NotNull
    @Valid
    private QualityDtoSelect qualities;

    @NotNull
    @Valid
    private StatusDtoSelect statuses;

    @NotNull
    @Valid
    private ActiveDtoSelect actives;

    @NotNull
    @Valid
    private ImageTypeDtoSelect imageTypes;
}
