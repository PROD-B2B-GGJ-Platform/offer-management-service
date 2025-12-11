package com.platform.talent.offer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    
    @Id
    private String offerId;
    
    @Column(nullable = false)
    private String organizationId;
    
    @Column(nullable = false)
    private String candidateId;
    
    @Column(nullable = false)
    private String jobId;
    
    private String applicationId;
    
    @Column(nullable = false)
    private String position;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private BigDecimal salary;
    
    private String currency;
    private BigDecimal bonus;
    private String benefits;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate offerDate;
    
    private LocalDate expiryDate;
    private LocalDate acceptedDate;
    private LocalDate rejectedDate;
    
    @Column(nullable = false)
    private String status;
    
    @Column(length = 2000)
    private String notes;
    
    private String createdBy;
    private LocalDate createdDate;
}

