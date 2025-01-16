package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private String resource;
    private String method;
    private String ipAddress;
    private Date timestamp;

    @Column(length = 1000)
    private String methodArguments; // Store method arguments as a string
    private String exceptionMessage; // Store exception message (if any)


}
