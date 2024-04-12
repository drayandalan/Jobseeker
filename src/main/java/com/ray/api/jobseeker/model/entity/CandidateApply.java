package com.ray.api.jobseeker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "candidate_apply")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateApply {
    @Id
    private String id;
    @Field("apply_id")
    private Integer applyId;
    @Field("candidate_id")
    private Integer candidateId;
    @Field("vacancy_id")
    private Integer vacancyId;
    @Field("apply_date")
    private String applyDate;
    @Field("created_date")
    private String createdDate;
}
