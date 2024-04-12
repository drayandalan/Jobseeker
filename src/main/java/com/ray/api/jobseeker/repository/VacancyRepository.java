package com.ray.api.jobseeker.repository;

import com.ray.api.jobseeker.model.entity.Vacancy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends MongoRepository<Vacancy, String> {
}
