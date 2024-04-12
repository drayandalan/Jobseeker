package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.GeneralDTO;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;

import java.util.List;

public interface FindAllService {

    List<Candidate> findAllCandidate(String collection);
    List<Vacancy> findAllVacancy(String collection);
    List<CandidateApply> findAllCandidateApply(String collection);
    List<Candidate> filterCandidate(String collection, GeneralDTO.RequestFilter requestFilter);
    List<Vacancy> filterVacancy(String collection, GeneralDTO.RequestFilter requestFilter);
    List<CandidateApply> filterCandidateApply(String collection, GeneralDTO.RequestFilter requestFilter);

}
