package com.ray.api.jobseeker.service.impl;

import com.ray.api.jobseeker.model.dto.GeneralDTO;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;
import com.ray.api.jobseeker.repository.CandidateApplyRepository;
import com.ray.api.jobseeker.repository.CandidateRepository;
import com.ray.api.jobseeker.repository.VacancyRepository;
import com.ray.api.jobseeker.service.FeatureService;
import com.ray.api.jobseeker.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private RepositoryUtil repositoryUtil;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private CandidateApplyRepository candidateApplyRepository;

    @Override
    public GeneralDTO.Response addCandidateApply(CandidateApply candidateApply) {
        Boolean success = repositoryUtil.setApplicationData(candidateApply);

        if (success)
            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully added candidate_apply data.").build();

        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to add candidate_apply data.").build();
    }

    @Override
    public GeneralDTO.Response addCandidate(Candidate candidate) {
        try {
            candidateRepository.save(candidate);
            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully added candidate data.").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to add candidate data.").build();
    }

    @Override
    public GeneralDTO.Response addVacancy(Vacancy vacancy) {
        try {
            vacancyRepository.save(vacancy);
            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully added vacancy data.").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to add vacancy data.").build();
    }

    @Override
    public GeneralDTO.Response updateCandidateApply(CandidateApply candidateApply) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("apply_id").is(candidateApply.getApplyId()));
            Update update = new Update();
            if (candidateApply.getCandidateId() != null) update.set("candidate_id", candidateApply.getCandidateId());
            if (candidateApply.getVacancyId() != null) update.set("vacancy_id", candidateApply.getVacancyId());
            if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, CandidateApply.class);

            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully updated candidate apply data for apply_id: "+candidateApply.getApplyId()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to update candidate apply data for apply_id: "+candidateApply.getApplyId()).build();
    }

    @Override
    public GeneralDTO.Response updateCandidate(Candidate candidate) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("candidate_id").is(candidate.getCandidateId()));
            Update update = new Update();
            if (candidate.getFullName() != null) update.set("full_name", candidate.getFullName());
            if (candidate.getDob() != null) update.set("dob", candidate.getDob());
            if (candidate.getGender() != null) update.set("gender", candidate.getGender());
            if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, Candidate.class);
            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully updated candidate data for candidate_id: "+candidate.getCandidateId()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to update candidate data for candidate_id: \"+candidate.getCandidateId()").build();
    }

    @Override
    public GeneralDTO.Response updateVacancy(Vacancy vacancy) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("vacancy_id").is(vacancy.getVacancyId()));
            Update update = new Update();
            if (vacancy.getVacancyName() != null) update.set("vacancy_name", vacancy.getVacancyName());
            if (vacancy.getMinAge() != null) update.set("min_age", vacancy.getMinAge());
            if (vacancy.getMaxAge() != null) update.set("max_age", vacancy.getMaxAge());
            if (vacancy.getRequirementGender() != null) update.set("requirement_gender", vacancy.getRequirementGender());
            if (vacancy.getExpiredDate() != null) update.set("expired_date", vacancy.getExpiredDate());
            if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, Vacancy.class);
            return GeneralDTO.Response.builder()
                    .error(Boolean.FALSE)
                    .message("Successfully updated vacancy data for vacancy_id: "+vacancy.getVacancyId()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to update candidate data for candidate_id: \"+candidate.getCandidateId()").build();
    }

    @Override
    public GeneralDTO.Response deleteCandidateApply(Integer applyId) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("apply_id").is(applyId));
            CandidateApply candidateApply = mongoTemplate.findOne(query, CandidateApply.class);
            if (candidateApply != null) {
                candidateApplyRepository.deleteById(candidateApply.getId());
                return GeneralDTO.Response.builder()
                        .error(Boolean.FALSE)
                        .message("Successfully deleted from candidate apply with apply_id: "+applyId)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to delete from candidate apply with apply_id: "+applyId)
                .build();
    }

    @Override
    public GeneralDTO.Response deleteCandidate(Integer candidateId) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("candidate_id").is(candidateId));
            Candidate candidate = mongoTemplate.findOne(query, Candidate.class);
            if (candidate != null) {
                candidateRepository.deleteById(candidate.getId());
                return GeneralDTO.Response.builder()
                        .error(Boolean.FALSE)
                        .message("Successfully deleted from candidate with candidate_id: "+candidateId)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to delete from candidate with candidate_id: "+candidateId)
                .build();
    }

    @Override
    public GeneralDTO.Response deleteVacancy(Integer vacancyId) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("vacancy_id").is(vacancyId));
            Vacancy vacancy = mongoTemplate.findOne(query, Vacancy.class);
            if (vacancy != null) {
                vacancyRepository.deleteById(vacancy.getId());
                return GeneralDTO.Response.builder()
                        .error(Boolean.FALSE)
                        .message("Successfully deleted from vacancy with vacancy_id: "+vacancyId)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to delete from vacancy with vacancy_id: "+vacancyId)
                .build();
    }
}
