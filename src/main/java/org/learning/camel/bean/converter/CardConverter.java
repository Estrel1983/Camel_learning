package org.learning.camel.bean.converter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.learning.camel.entity.MtgCardInsertRequest;

@Converter
public class CardConverter {

    @Converter
    public static MtgCardInsertRequest toMtgCardInsertRequest(byte [] input, Exchange exchange){
        TypeConverter convertor = exchange.getContext().getTypeConverter();
        String inputString = convertor.convertTo(String.class, input);
        JsonObject inputJson = JsonParser.parseString(inputString).getAsJsonObject();
        MtgCardInsertRequest result = new MtgCardInsertRequest();
        result.setCardName(inputJson.get("cardName").getAsString());
        result.setArtistName(inputJson.get("artistName").getAsString());
        result.setSetName(inputJson.get("setName").getAsString());
        result.setFoil(inputJson.get("foil").getAsBoolean());
        return result;
    }

    @Converter
    public static String toStringQuery(MtgCardInsertRequest request){
        return "INSERT INTO public.cards(card_name, artist, set_name, foil) VALUES ('" +
                request.getCardName() + "','" + request.getArtistID() + "','" + request.getSetId() + "','" + request.getFoil() +"');";
    }
}
