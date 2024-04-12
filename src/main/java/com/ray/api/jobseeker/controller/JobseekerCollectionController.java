package com.ray.api.jobseeker.controller;

import com.ray.api.jobseeker.model.dto.GeneralDTO;
import com.ray.api.jobseeker.model.entity.Candidate;
import com.ray.api.jobseeker.model.entity.CandidateApply;
import com.ray.api.jobseeker.model.entity.Vacancy;
import com.ray.api.jobseeker.service.FeatureService;
import com.ray.api.jobseeker.service.FindAllService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class JobseekerCollectionController {

    @Autowired
    private FindAllService findAllService;
    @Autowired
    private FeatureService featureService;

    @GetMapping("/jobseeker-api/findAll")
    public ResponseEntity<?> findAll(@RequestParam(name = "collection") String collection) {
        log.info("Collection {}", collection);
        List<Object> response = switch (collection.toLowerCase()) {
            case "candidate" -> Collections.singletonList(findAllService.findAllCandidate(collection));
            case "vacancy" -> Collections.singletonList(findAllService.findAllVacancy(collection));
            case "candidate_apply" -> Collections.singletonList(findAllService.findAllCandidateApply(collection));
            default -> Collections.singletonList(new ArrayList<>());
        };
        log.info("Response: {}", response);
        return ResponseEntity.ok(response.get(0));
    }

    @PostMapping("/jobseeker-api/getDataFilter")
    public ResponseEntity<?> getDataFilter(@RequestParam(name = "collection") String collection,
                                           @RequestBody GeneralDTO.RequestFilter requestFilter) {
        log.info("Collection {}", collection);
        List<Object> response = switch (collection.toLowerCase()) {
            case "candidate" -> Collections.singletonList(findAllService.filterCandidate(collection, requestFilter));
            case "vacancy" -> Collections.singletonList(findAllService.filterVacancy(collection, requestFilter));
            case "candidate_apply" -> Collections.singletonList(findAllService.filterCandidateApply(collection, requestFilter));
            default -> Collections.singletonList(new ArrayList<>());
        };
        log.info("Response: {}", response);
        return ResponseEntity.ok(response.get(0));
    }

    @PostMapping("/jobseeker-api/addCandidate")
    public ResponseEntity<?> addCandidate(@RequestBody Candidate candidate) {
        log.info("Data: {}", candidate);
        if (candidate.getCandidateId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Candidate id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zoneId);
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(candidate.getDob(), formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(GeneralDTO.Response.builder()
                    .error(Boolean.TRUE)
                    .message("Dob is not in valid format.").build());
        }
        candidate.setId(null);
        candidate.setDob(localDate.format(formatter));

        try {
            response = featureService.addCandidate(candidate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/addVacancy")
    public ResponseEntity<?> addVacancy(@RequestBody Vacancy vacancy) {
        log.info("Data: {}", vacancy);
        if (vacancy.getVacancyId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Vacancy id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zoneId);
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(vacancy.getExpiredDate(), formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(GeneralDTO.Response.builder()
                    .error(Boolean.TRUE)
                    .message("Expired date is not in valid format.").build());
        }
//        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        vacancy.setId(null);
        vacancy.setCreatedDate(LocalDateTime.now(zoneId).format(formatterDateTime));
        vacancy.setExpiredDate(localDate.format(formatter));

        try {
            response = featureService.addVacancy(vacancy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/addCandidateApply")
    public ResponseEntity<?> addCandidateApply(@RequestBody CandidateApply candidateApply) {
        log.info("Data: {}", candidateApply);
        if (candidateApply.getApplyId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Apply id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        candidateApply.setId(null);
        candidateApply.setApplyDate(LocalDate.now(zoneId).format(formatter));
        candidateApply.setCreatedDate(LocalDateTime.now(zoneId).format(formatterDateTime));

        try {
            response = featureService.addCandidateApply(candidateApply);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/updateCandidate")
    public ResponseEntity<?> updateCandidate(@RequestBody Candidate candidate) {
        log.info("Data: {}", candidate);
        if (candidate.getCandidateId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Candidate id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zoneId);
        LocalDate localDate;
        if (candidate.getDob() != null) {
            try {
                localDate = LocalDate.parse(candidate.getDob(), formatter);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.ok(GeneralDTO.Response.builder()
                        .error(Boolean.TRUE)
                        .message("Dob is not in valid format.").build());
            }
            candidate.setDob(localDate.format(formatter));
        }
        candidate.setId(null);

        try {
            response = featureService.updateCandidate(candidate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/updateVacancy")
    public ResponseEntity<?> updateVacancy(@RequestBody Vacancy vacancy) {
        log.info("Data: {}", vacancy);
        if (vacancy.getVacancyId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Vacancy id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zoneId);
        LocalDate localDate;
        if (vacancy.getExpiredDate() != null) {
            try {
                localDate = LocalDate.parse(vacancy.getExpiredDate(), formatter);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.ok(GeneralDTO.Response.builder()
                        .error(Boolean.TRUE)
                        .message("Expired date is not in valid format.").build());
            }
            //        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            vacancy.setExpiredDate(localDate.format(formatter));
        }
        vacancy.setId(null);

        try {
            response = featureService.updateVacancy(vacancy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/updateCandidateApply")
    public ResponseEntity<?> updateCandidateApply(@RequestBody CandidateApply candidateApply) {
        log.info("Data: {}", candidateApply);
        if (candidateApply.getApplyId() == null) return ResponseEntity.ok(GeneralDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Apply id can't be null or empty.").build());

        GeneralDTO.Response response = GeneralDTO.Response.builder().build();
        candidateApply.setId(null);

        try {
            response = featureService.updateCandidateApply(candidateApply);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobseeker-api/deleteData")
    public ResponseEntity<?> deleteData(@RequestParam(name = "collection") String collection,
                                        @RequestParam(name = "id") Integer id) {
        log.info("{} {}", collection, id);
        GeneralDTO.Response response = switch (collection.toLowerCase()) {
            case "candidate" -> featureService.deleteCandidate(id);
            case "vacancy" -> featureService.deleteVacancy(id);
            case "candidate_apply" -> featureService.deleteCandidateApply(id);
            default -> GeneralDTO.Response.builder().build();
        };
        log.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
