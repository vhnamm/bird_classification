package com.hnv.BirdBE.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PredictionResponse {


    private Long predictionId;
    private String originFileName;
    private TopBirdResponse topBirdDetail;
    private List<BirdConfidenceDTO> top5;

}
