package com.hnv.BirdBE.service;

import com.hnv.BirdBE.IOCContainer.AppConfig;
import com.hnv.BirdBE.dto.response.*;
import com.hnv.BirdBE.entity.Bird;
import com.hnv.BirdBE.entity.Prediction;
import com.hnv.BirdBE.entity.PredictionDetail;
import com.hnv.BirdBE.repository.BirdRepository;
import com.hnv.BirdBE.repository.PredictRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionImpl implements PredictionService{
    private final PredictRepository predictRepository;
    private final BirdRepository birdRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.python.service.url}")
    private String pythonURL;

    @Override
    @Transactional
    public PredictionResponse getPredictions(MultipartFile file) {
        AIServiceResponse predictResponse = callPythonAPI(file);

        Prediction prediction = new Prediction();
        prediction.setOriginFileName(file.getOriginalFilename());
        prediction.setCreatedAt(LocalDateTime.now());

        List<PredictionDetail> list = new ArrayList<>();

        for(int i=0; i<predictResponse.getPredictedBird().size(); i++){
            AIBirdResult birdResult = predictResponse.getPredictedBird().get(i);

            Bird b = birdRepository.findByBirdCode(birdResult.getBirdCode());

            PredictionDetail predictionDetail = new PredictionDetail();
            predictionDetail.setBird(b);
            predictionDetail.setPrediction(prediction);
            predictionDetail.setConfidenceRate(birdResult.getConfidenceRate());
            predictionDetail.setRank(i + 1);
            list.add(predictionDetail);


        }

        prediction.setPredictionDetails(list);
        predictRepository.save(prediction);

        return mapToDTO(prediction, list);
    }

    private AIServiceResponse callPythonAPI(MultipartFile file){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            body.add("audio_file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được file âm thanh", e);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AIServiceResponse> response = restTemplate.exchange(
                pythonURL,
                HttpMethod.POST,
                request,
                AIServiceResponse.class
        );


        return response.getBody();
    }

    private PredictionResponse mapToDTO(Prediction prediction, List<PredictionDetail> detailList){
        PredictionResponse predictionResponse = new PredictionResponse();
        predictionResponse.setPredictionId(prediction.getId());
        predictionResponse.setOriginFileName(prediction.getOriginFileName());

        // Top 1
        PredictionDetail top1 = detailList.get(0);
        Bird top1Bird = top1.getBird();

        TopBirdResponse topBirdDetail = new TopBirdResponse();
        topBirdDetail.setBirdCode(top1Bird.getBirdCode());
        topBirdDetail.setVietnameseName(top1Bird.getVietnameseName());
        topBirdDetail.setSciencetificName(top1Bird.getSciencetificName());
        topBirdDetail.setDescription(top1Bird.getDescription());
        topBirdDetail.setImgURL(top1Bird.getImgURL());
        topBirdDetail.setConfidenceRate(top1.getConfidenceRate());

        predictionResponse.setTopBirdDetail(topBirdDetail);

        // Top 5
        List<BirdConfidenceDTO> top5 = new ArrayList<>();
        for (PredictionDetail d : detailList) {
            BirdConfidenceDTO dto = new BirdConfidenceDTO();
            dto.setBirdCode(d.getBird().getBirdCode());
            dto.setVietnameseName(d.getBird().getVietnameseName());
            dto.setConfidence(d.getConfidenceRate());
            top5.add(dto);
        }

        predictionResponse.setTop5(top5);

        return predictionResponse;
    }

}
