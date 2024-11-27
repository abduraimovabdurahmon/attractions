package com.example.attractions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(name = "Attraction request DTO")
public class AttractionRequestDto {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String category;
    private Double rating;
    private String website;
}
