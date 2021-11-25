package com.tw.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "task_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task extends EntityBase {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", columnDefinition = "ENUM('EASY', 'MEDIUM', 'HARD', 'HELL')")
    private Level level;

    @Column(name = "description")
    private String description;


    private String userId; //todo userId

}
