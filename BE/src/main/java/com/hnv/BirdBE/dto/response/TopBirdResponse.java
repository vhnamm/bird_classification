package com.hnv.BirdBE.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@NoArgsConstructor
@Getter
@Setter
public class TopBirdResponse {

    private Long id;

    private String birdCode;

    private String sciencetificName;


    private String vietnameseName;


    private String description;


    private String imgURL;
    private Double confidenceRate;
}
