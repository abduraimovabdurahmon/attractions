package com.example.attractions.repository;

import com.example.attractions.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttractionRepository extends JpaRepository<Attraction, Long>{

}
