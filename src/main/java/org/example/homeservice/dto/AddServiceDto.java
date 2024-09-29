package org.example.dto;

import lombok.Data;

@Data
public class AddServiceDto {

    private Long id;
    private String name;
    private String description;
    private Float basePrice;
    private Long parentServiceId;  //todo does need whole parent object?
    private boolean isCategory;
}
