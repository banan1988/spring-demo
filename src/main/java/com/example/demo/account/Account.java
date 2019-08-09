package com.example.demo.account;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts")
class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    @Builder.Default
    private String role = "ROLE_USER";

    @CreatedDate
    private LocalDateTime createdAt;

}