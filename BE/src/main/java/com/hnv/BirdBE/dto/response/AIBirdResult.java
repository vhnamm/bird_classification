package com.hnv.BirdBE.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class AIBirdResult {
    @JsonProperty("bird_code")
    private String birdCode;

    @JsonProperty("confidence")
    private Double confidenceRate;
}
