package com.hnv.BirdBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "bird")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bird {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bird_code", length = 12, unique = true, nullable = false)
    private String birdCode;

    @Column(name = "sciencetific_name", length = 50, unique = true, nullable = false)
    private String sciencetificName;

    @Column(name = "vietnamese_name", length = 50)
    @Nationalized
    private String vietnameseName;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(name = "img_url")
    private String imgULR;

}
