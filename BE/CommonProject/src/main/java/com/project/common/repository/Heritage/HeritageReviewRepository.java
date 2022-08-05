package com.project.common.repository.Heritage;

import com.project.common.entity.Heritage.HeritageReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeritageReviewRepository extends JpaRepository<HeritageReviewEntity, Integer> {

    HeritageReviewEntity findByHeritageReviewSeq(int heritageReviewSeq);

    void deleteByHeritageReviewSeq(int heritageReviewSeq);
}
