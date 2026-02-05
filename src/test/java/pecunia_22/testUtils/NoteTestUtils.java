package pecunia_22.testUtils;

import pecunia_22.models.*;

import java.sql.Date;

public class NoteTestUtils {

    public static Note createSampleNote(
            Currency currency,
            Bought bought,
            Active active,
            Status status,
            Quality quality,
            Making making,
            ImageType imageType
    ) {
        Note note = new Note();
        note.setCurrencies(currency);
        note.setDenomination(100.0);
        note.setDateBuy(new Date(System.currentTimeMillis())); // dzisiejsza data, można też podać konkretną        note.setNameCurrency("TestNote");
        note.setSeries("TEST");
        note.setBoughts(bought);
        note.setItemDate("2020");
        note.setQuantity(1);
        note.setUnitQuantity("szt");
        note.setActives(active);
        note.setPriceBuy(10.0);
        note.setPriceSell(15.0);
        note.setMakings(making);
        note.setQualities(quality);
        note.setWidth(100);
        note.setHeight(50);
        note.setStatuses(status);
        note.setImageTypes(imageType);
        note.setStatusSell(null);
        note.setVisible(true);
        note.setDescription("Test note description");
        note.setAversPath("");
        note.setReversePath("");
        note.setSerialNumber("ABC123");
        return note;
    }
}
