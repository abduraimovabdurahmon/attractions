package com.example.attractions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;



@Entity
@Getter
@Setter
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "point", columnDefinition = "geography(Point,4326)")
    private Point point;

    @Column(name = "category")
    private String category;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "website")
    private String website;


    public Attraction() {
    }

    public Attraction(String name, String description, Point point, String category, Double rating, String website) {
        this.name = name;
        this.description = description;
        this.point = point;
        this.category = category;
        this.rating = rating;
        this.website = website;
    }

}
