package com.hnv.BirdBE.repository;

import com.hnv.BirdBE.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictRepository extends JpaRepository<Prediction, Long> {
}
