package com.hnv.BirdBE.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BirdConfidenceDTO {
    private String birdCode;
    private String vietnameseName;
    private String scienceName;
    private Double confidence;
}
