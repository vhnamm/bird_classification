package com.hnv.BirdBE.repository;

import com.hnv.BirdBE.entity.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirdRepository extends JpaRepository<Bird, Long> {
    Bird findByBirdCode(String birdCode);
}
