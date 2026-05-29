package com.hnv.BirdBE.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AIServiceResponse {
    @JsonProperty("predicted_bird")
    private List<AIBirdResult> predictedBird;
}
