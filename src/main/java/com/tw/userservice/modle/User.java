package com.tw.userservice.modle;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import java.time.Instant;


@Entity
@Data
@Table(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends EntityBase {
    @Id
    @Column(name = "id", nullable = false)
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "status")
    private Boolean status;

    private String name;

    private String role;

    private Integer age;

    private String cellphone;

    private String address;

    private String email;


}
