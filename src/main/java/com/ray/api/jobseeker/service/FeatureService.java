package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.GeneralDTO;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;

public interface FeatureService {

    GeneralDTO.Response addCandidateApply(CandidateApply candidateApply);
    GeneralDTO.Response addCandidate(Candidate candidate);
    GeneralDTO.Response addVacancy(Vacancy vacancy);
    GeneralDTO.Response updateCandidateApply(CandidateApply candidateApply);
    GeneralDTO.Response updateCandidate(Candidate candidate);
    GeneralDTO.Response updateVacancy(Vacancy vacancy);
    GeneralDTO.Response deleteCandidateApply(Integer applyId);
    GeneralDTO.Response deleteCandidate(Integer candidateId);
    GeneralDTO.Response deleteVacancy(Integer vacancyId);
}
