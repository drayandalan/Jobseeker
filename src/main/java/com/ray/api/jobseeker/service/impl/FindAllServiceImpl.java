package com.ray.api.jobseeker.service.impl;

import com.ray.api.jobseeker.model.dto.GeneralDTO;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;
import com.ray.api.jobseeker.service.FindAllService;
import com.ray.api.jobseeker.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FindAllServiceImpl implements FindAllService {

    @Autowired
    private RepositoryUtil repositoryUtil;

    @Override
    public List<Candidate> findAllCandidate(String collection) {
        List<Candidate> response = new ArrayList<>();

        try {
            response = repositoryUtil.findAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Candidates: {}", response);

        return response;
    }

    @Override
    public List<Vacancy> findAllVacancy(String collection) {
        List<Vacancy> response = new ArrayList<>();

        try {
            response = repositoryUtil.findAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<CandidateApply> findAllCandidateApply(String collection) {
        List<CandidateApply> response = new ArrayList<>();

        try {
            response = repositoryUtil.findAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<Candidate> filterCandidate(String collection, GeneralDTO.RequestFilter requestFilter) {
        List<Candidate> response = new ArrayList<>();

        try {
            response = repositoryUtil.filterResult(
                    collection,
                    requestFilter.getKeyword(),
                    requestFilter.getCandidateId(),
                    requestFilter.getVacancyId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<Vacancy> filterVacancy(String collection, GeneralDTO.RequestFilter requestFilter) {
        List<Vacancy> response = new ArrayList<>();

        try {
            response = repositoryUtil.filterResult(
                    collection,
                    requestFilter.getKeyword(),
                    requestFilter.getCandidateId(),
                    requestFilter.getVacancyId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<CandidateApply> filterCandidateApply(String collection, GeneralDTO.RequestFilter requestFilter) {
        List<CandidateApply> response = new ArrayList<>();

        try {
            response = repositoryUtil.filterResult(
                    collection,
                    requestFilter.getKeyword(),
                    requestFilter.getCandidateId(),
                    requestFilter.getVacancyId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
