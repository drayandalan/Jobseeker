package com.ray.api.jobseeker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "vacancy")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    private String id;
    @Field("vacancy_id")
    private Integer vacancyId;
    @Field("vacancy_name")
    private String vacancyName;
    @Field("min_age")
    private Integer minAge;
    @Field("max_age")
    private Integer maxAge;
    @Field("requirement_gender")
    private String requirementGender;
    @Field("created_date")
    private String createdDate;
    @Field("expired_date")
    private String expiredDate;
}
