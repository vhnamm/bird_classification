package com.hnv.BirdBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prediction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "file_name")
    private String originFileName;

    @OneToMany(mappedBy = "prediction", cascade = CascadeType.ALL)
    private List<PredictionDetail> predictionDetails;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
