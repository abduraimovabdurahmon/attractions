package com.example.attractions.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AttractionResponceDto {
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String category;
    private Double rating;
    private String website;
}
