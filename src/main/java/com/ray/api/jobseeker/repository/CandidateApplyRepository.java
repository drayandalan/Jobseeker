package com.ray.api.jobseeker.repository;

import com.ray.api.jobseeker.model.entity.CandidateApply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateApplyRepository extends MongoRepository<CandidateApply, String> {
}
