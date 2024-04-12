package com.ray.api.jobseeker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "candidate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    private String id;
    @Field("candidate_id")
    private Integer candidateId;
    @Field("full_name")
    private String fullName;
    private String dob;
    private String gender;
}
