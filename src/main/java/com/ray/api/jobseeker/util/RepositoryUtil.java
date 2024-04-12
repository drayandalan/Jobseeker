package com.ray.api.jobseeker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;
import com.ray.api.jobseeker.repository.CandidateApplyRepository;
import com.ray.api.jobseeker.repository.CandidateRepository;
import com.ray.api.jobseeker.repository.VacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RepositoryUtil {

    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
    @Autowired private CandidateRepository candidateRepository;
    @Autowired private VacancyRepository vacancyRepository;
    @Autowired private CandidateApplyRepository candidateApplyRepository;
    @Autowired private MongoTemplate mongoTemplate;

    public <T> List<T> findAll(String collection) {
        try {
            if (collection.equalsIgnoreCase("candidate"))
                return (List<T>) candidateRepository.findAll();
            if (collection.equalsIgnoreCase("vacancy"))
                return (List<T>) vacancyRepository.findAll();
            if (collection.equalsIgnoreCase("candidate_apply"))
                return (List<T>) candidateApplyRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Boolean setApplicationData(CandidateApply candidateApply) {
        try {
            candidateApplyRepository.save(candidateApply);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public <T> List<T> filterResult(String collection, String keyword, Integer candidateId, Integer vacancyId) {
        try {
            if ((collection.equalsIgnoreCase("candidate") || collection.equalsIgnoreCase("vacancy"))
                    && (keyword == null || keyword.isEmpty()))
                throw new JobseekerException("Keyword is missing.");

            Criteria criteria = new Criteria();
            Sort sort;
            List<Sort.Order> orders = new ArrayList<>();
            Query query = new Query();
            if (collection.equalsIgnoreCase("candidate")) {
                criteria.orOperator(
                        Criteria.where("full_name").regex(".*" + Pattern.quote(keyword) + ".*", "i"),
                        Criteria.where("gender").regex(".*" + Pattern.quote(keyword) + ".*", "i")
                );

                orders.add(Sort.Order.asc("full_name"));
                orders.add(Sort.Order.asc("dob"));
                orders.add(Sort.Order.asc("gender"));

                sort = Sort.by(orders);
                query.addCriteria(criteria);
                query.with(sort);
                return (List<T>) mongoTemplate.find(query, Candidate.class);
            }
            if (collection.equalsIgnoreCase("vacancy")) {
                criteria.orOperator(
                        Criteria.where("vacancy_name").regex(".*" + Pattern.quote(keyword) + ".*", "i"),
                        Criteria.where("requirement_gender").regex(".*" + Pattern.quote(keyword) + ".*", "i")
                );

                orders.add(Sort.Order.asc("vacancy_name"));
                orders.add(Sort.Order.asc("min_age"));
                orders.add(Sort.Order.asc("max_age"));
                orders.add(Sort.Order.asc("requirement_gender"));

                sort = Sort.by(orders);
                query.addCriteria(criteria);
                query.with(sort);
                return (List<T>) mongoTemplate.find(query, Vacancy.class);
            }
            if (collection.equalsIgnoreCase("candidate_apply")) {
                if (candidateId == null) candidateId = 0;
                if (vacancyId == null) vacancyId = 0;

                criteria.orOperator(
                        Criteria.where("candidate_id").is(candidateId),
                        Criteria.where("vacancy_id").is(vacancyId)
                );
                query.addCriteria(criteria);
                return (List<T>) mongoTemplate.find(query, CandidateApply.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
