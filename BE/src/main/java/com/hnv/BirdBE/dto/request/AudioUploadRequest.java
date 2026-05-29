package com.hnv.BirdBE.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class AudioUploadRequest {
    private MultipartFile audioFile;
}
