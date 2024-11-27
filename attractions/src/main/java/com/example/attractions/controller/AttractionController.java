package com.example.attractions.controller;


import com.example.attractions.dto.AttractionRequestDto;
import com.example.attractions.dto.AttractionResponceDto;
import com.example.attractions.model.Attraction;
import com.example.attractions.repository.AttractionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionRepository attractionRepository;

    AttractionRequestDto attractionRequestDto = new AttractionRequestDto();



    @PostMapping
    @Operation(
            summary = "Yangi diqqatga sazovor joyni qo'shish.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Diqqatga sazovor joyni qo'shildi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Muvaffaqqiyatli yaratildi!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Maydonlarning hammasi to'ldirilmagan.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Iltimos barcha maydonlarni to'ldiring!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ichki server xatosi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Ichki server xatosi!"
                                            )
                                    }
                            )
                    )
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttractionRequestDto.class),
                    examples = {
                            @ExampleObject(
                                    value = "{\n" +
                                            "\t\t\"name\": \"Ichan Qala\",\n" +
                                            "\t\t\"description\": \"Ichan qalʼa — Oʻzbekistonning Xiva shahrining ichki shaharchasi. 1990 yildan beri u Jahon merosi ob'ekti sifatida himoyalangan.\",\n" +
                                            "\t\t\"latitude\": 41.3781,\n" +
                                            "\t\t\"longitude\": 60.3593,\n" +
                                            "\t\t\"category\": \"tarixiy\",\n" +
                                            "\t\t\"rating\": 4.5,\n" +
                                            "\t\t\"website\": \"https://ichanqala.uz/main/\"\n" +
                                            "\t}"
                            )
                    }
            )
    )
    public String createAttraction(@RequestBody AttractionRequestDto attractionRequestDto) {

        if (attractionRequestDto.getName() == null || attractionRequestDto.getDescription() == null || attractionRequestDto.getLatitude() == null || attractionRequestDto.getLongitude() == null || attractionRequestDto.getCategory() == null || attractionRequestDto.getRating() == null || attractionRequestDto.getWebsite() == null) {
            return "Iltimos barcha maydonlarni to'ldiring!";
        }

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(attractionRequestDto.getLatitude(), attractionRequestDto.getLongitude()));

        Attraction attraction = new Attraction();

        attraction.setName(attractionRequestDto.getName());
        attraction.setDescription(attractionRequestDto.getDescription());
        attraction.setPoint(point);
        attraction.setCategory(attractionRequestDto.getCategory());
        attraction.setRating(attractionRequestDto.getRating());
        attraction.setWebsite(attractionRequestDto.getWebsite());

        attractionRepository.save(attraction);
        return "Muvaffaqqiyatli yaratildi!";
    }


    @CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
    @GetMapping
    @Operation(
            summary = "Barcha diqqatga sazovor joylar ro'yxati.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Diqqatga sazovor joylar ro'yxati.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AttractionResponceDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "["+
                                                            "{\n" +
                                                            "\t\"id\": 1,\n" +
                                                            "\t\"name\": \"Ichan Qal'a\",\n" +
                                                            "\t\"description\": \"Ichan qalʼa — Oʻzbekistonning Xiva shahrining ichki shaharchasi. 1990 yildan beri u Jahon merosi ob'ekti sifatida himoyalangan.\",\n" +
                                                            "\t\"latitude\": 41.3781,\n" +
                                                            "\t\"longitude\": 60.3593,\n" +
                                                            "\t\"category\": \"Historical\",\n" +
                                                            "\t\"rating\": 4.5,\n" +
                                                            "\t\"website\": \"https://ichanqala.uz/main/\"\n" +
                                                            "}"
                                                            + "]"


                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<?> getAllAttractions() {
        List<Attraction> attractions = attractionRepository.findAll();
        List<AttractionResponceDto> attractionResponceDtos = new ArrayList<>();

        for (Attraction attraction : attractions) {
            AttractionResponceDto attractionResponceDto = new AttractionResponceDto();
            attractionResponceDto.setId(attraction.getId());
            attractionResponceDto.setName(attraction.getName());
            attractionResponceDto.setDescription(attraction.getDescription());
            attractionResponceDto.setLatitude(attraction.getPoint().getX());
            attractionResponceDto.setLongitude(attraction.getPoint().getY());
            attractionResponceDto.setCategory(attraction.getCategory());
            attractionResponceDto.setRating(attraction.getRating());
            attractionResponceDto.setWebsite(attraction.getWebsite());

            attractionResponceDtos.add(attractionResponceDto);
        }

        return ResponseEntity.ok(attractionResponceDtos);

    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Diqqatga sazovor joyni yangilash.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Diqqatga sazovor joy yangilandi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Muvaffaqqiyatli yangilandi!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ichki server xatosi yoki barcha maydonlar to'ldirilmagan.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Ichki server xatosi yoki barcha maydonlar to'ldirilmagan!"
                                            )
                                    }
                            )
                    )
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttractionRequestDto.class),
                    examples = {
                            @ExampleObject(
                                    value = "{\n" +
                                            "\t\t\"name\": \"Ichan Qala\",\n" +
                                            "\t\t\"description\": \"Ichan qalʼa — Oʻzbekistonning Xiva shahrining ichki shaharchasi. 1990 yildan beri u Jahon merosi ob'ekti sifatida himoyalangan.\",\n" +
                                            "\t\t\"latitude\": 41.3781,\n" +
                                            "\t\t\"longitude\": 60.3593,\n" +
                                            "\t\t\"category\": \"tarixiy\",\n" +
                                            "\t\t\"rating\": 4.5,\n" +
                                            "\t\t\"website\": \"https://ichanqala.uz/main/\"\n" +
                                            "\t}"
                            )
                    }
            )
    )
    public String updateAttraction(@PathVariable Long id, @RequestBody AttractionRequestDto attractionRequestDto) {
        if (attractionRequestDto.getName() == null || attractionRequestDto.getDescription() == null || attractionRequestDto.getLatitude() == null || attractionRequestDto.getLongitude() == null || attractionRequestDto.getCategory() == null || attractionRequestDto.getRating() == null || attractionRequestDto.getWebsite() == null) {
            return "Iltimos barcha maydonlarni to'ldiring!";
        }

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(attractionRequestDto.getLatitude(), attractionRequestDto.getLongitude()));

        Attraction attraction = attractionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Bunday id'li attraction topilmadi!")
        );




        attraction.setName(attractionRequestDto.getName());
        attraction.setDescription(attractionRequestDto.getDescription());
        attraction.setPoint(point);
        attraction.setCategory(attractionRequestDto.getCategory());
        attraction.setRating(attractionRequestDto.getRating());
        attraction.setWebsite(attractionRequestDto.getWebsite());

        attractionRepository.save(attraction);
        return "Muvaffaqqiyatli yangilandi!";
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Diqqatga sazovor joy o'chirish.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Diqqatga sazovor joy o'chirildi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Muvaffaqqiyatli o'chirildi!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ichki server xatosi yoki bunday id'li attraction topilmadi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Ichki server xatosi yoki bunday id'li attraction topilmadi!"
                                            )
                                    }
                            )
                    )
            }
    )
    public String deleteAttraction(@PathVariable Long id) {
        attractionRepository.deleteById(id);
        return "Muvaffaqqiyatli o'chirildi!";
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Diqqatga sazovor joyni olish.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Diqqatga sazovor joy.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AttractionResponceDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n" +
                                                            "\t\"id\": 1,\n" +
                                                            "\t\"name\": \"Ichan Qal'a\",\n" +
                                                            "\t\"description\": \"Ichan qalʼa — Oʻzbekistonning Xiva shahrining ichki shaharchasi. 1990 yildan beri u Jahon merosi ob'ekti sifatida himoyalangan.\",\n" +
                                                            "\t\"latitude\": 41.3781,\n" +
                                                            "\t\"longitude\": 60.3593,\n" +
                                                            "\t\"category\": \"Historical\",\n" +
                                                            "\t\"rating\": 4.5,\n" +
                                                            "\t\"website\": \"https://ichanqala.uz/main/\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ichki server xatosi yoki bunday id'li attraction topilmadi.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Ichki server xatosi yoki bunday id'li attraction topilmadi!"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<?> getAttraction(@PathVariable Long id) {
        Attraction attraction = attractionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Bunday id'li attraction topilmadi!")
        );

        AttractionResponceDto attractionResponceDto = new AttractionResponceDto();
        attractionResponceDto.setId(attraction.getId());
        attractionResponceDto.setName(attraction.getName());
        attractionResponceDto.setDescription(attraction.getDescription());
        attractionResponceDto.setLatitude(attraction.getPoint().getX());
        attractionResponceDto.setLongitude(attraction.getPoint().getY());
        attractionResponceDto.setCategory(attraction.getCategory());
        attractionResponceDto.setRating(attraction.getRating());
        attractionResponceDto.setWebsite(attraction.getWebsite());

        return ResponseEntity.ok(attractionResponceDto);
    }


}




















//    {
//        "name":"Ichan Qal'a",
//        "description":"Ichan Qal'a is the inner town of the city of Khiva, Uzbekistan. Since 1990, it has been protected as a World Heritage Site.",
//        "latitude":"41.3781",
//        "longitude":"60.3593",
//        "category":"Historical",
//        "rating":"4.5",
//        "website":"https://ichanqala.uz/main/"
//    }
