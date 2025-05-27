package org.learning.camel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class MtgCard {
    private Integer id;
    @JsonProperty("card_name")
    private String cardName;
    @JsonProperty("artist")
    private Integer artistID;
    @JsonProperty("set_name")
    private Integer setId;
    @JsonProperty("foil")
    private Boolean foil;
    @JsonProperty("link_to_image")
    private String linkToImage;
}
