package pecunia_22.models.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pecunia_22.models.dto.CommonDto;
import pecunia_22.models.dto.ImageType.ImageTypeDto;
import pecunia_22.models.dto.active.ActiveDto;
import pecunia_22.models.dto.bought.BoughtDto;
import pecunia_22.models.dto.making.MakingDto;
import pecunia_22.models.dto.quality.QualityDto;
import pecunia_22.models.dto.status.StatusDto;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class SecurityDtoByCurrency extends CommonDto {

    public SecurityDtoByCurrency(String itemDate, Date dateBuy, String nameCurrency, Double priceBuy, Double priceSell,
                                 Integer quantity, String statusSell, String description, String aversPath, String reversePath,
                                 Double denomination, String series, Integer width, Integer height, Timestamp created_at, Timestamp updated_at,
                                 Boolean visible, String unitCurrency, String unitQuantity, BoughtDto boughts, QualityDto qualities, StatusDto statuses,
                                 ActiveDto actives, ImageTypeDto imageTypes) {
        super(itemDate, dateBuy, nameCurrency, priceBuy, priceSell, quantity, statusSell, description,
                unitQuantity, visible, unitCurrency, aversPath, reversePath, created_at, updated_at, boughts, qualities, statuses, actives, imageTypes);
        this.denomination = denomination;
        this.series = series;
        this.width = width;
        this.height = height;
    }
    public SecurityDtoByCurrency() {
    }

    private Long id;
    private Double denomination;
    private String series;
    private Integer width;
    private Integer height;
    private MakingDto makings;
}
