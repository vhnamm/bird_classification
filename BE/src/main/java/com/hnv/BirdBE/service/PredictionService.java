package com.hnv.BirdBE.service;

import com.hnv.BirdBE.dto.response.PredictionResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PredictionService {
    PredictionResponse getPredictions(MultipartFile file);
}
