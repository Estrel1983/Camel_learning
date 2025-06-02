package org.learning.camel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Set {
    private Integer id;
    private String setName;
    private String setShortName;
}
