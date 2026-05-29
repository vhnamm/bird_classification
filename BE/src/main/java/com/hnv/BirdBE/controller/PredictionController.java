package com.hnv.BirdBE.controller;

import com.hnv.BirdBE.dto.response.PredictionResponse;
import com.hnv.BirdBE.service.PredictionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Slf4j  // thêm
public class PredictionController {
    private final PredictionService service;

    @PostMapping(path = "/api/predictions")
    public ResponseEntity<PredictionResponse> predict(@RequestParam(name = "file") MultipartFile file){
        log.info("=== CONTROLLER NHẬN REQUEST, file={} ===", file.getOriginalFilename());
        PredictionResponse result = service.getPredictions(file);
        return ResponseEntity.ok(result);
    }
}
