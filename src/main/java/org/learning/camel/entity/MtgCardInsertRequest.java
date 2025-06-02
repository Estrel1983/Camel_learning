package org.learning.camel.entity;

import lombok.Data;

@Data
public class MtgCardInsertRequest {
    private String cardName;
    private Integer artistID;
    private String artistName;
    private Integer setId;
    private String setName;
    private Boolean foil;
    private String linkToImage;
}
