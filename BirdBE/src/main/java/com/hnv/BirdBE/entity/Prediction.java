package com.hnv.BirdBE.entity;

import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Table(name = "prediction")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audio_url", nullable = false)
    private String audioURL;

    @Column(name = "file_name")
    private String originFileName;

    
}
