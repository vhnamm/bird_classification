package com.hnv.BirdBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prediction_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredictionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Bird bird;

    @ManyToOne
    private Prediction prediction;

    @Column(name = "confidence_rate", nullable = false)
    private Double confidenceRate;

    @Column(name = "rank_order")
    private Integer rank;
}
