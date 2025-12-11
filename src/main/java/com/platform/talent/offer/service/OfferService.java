package com.platform.talent.offer.service;

import com.platform.events.models.TalentOfferAcceptedEvent;
import com.platform.events.producer.EventPublisher;
import com.platform.talent.offer.domain.model.Offer;
import com.platform.talent.offer.domain.model.OfferStatus;
import com.platform.talent.offer.domain.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Offer Management Service
 * Handles offer lifecycle and triggers employee creation on acceptance
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final EventPublisher eventPublisher;
    private final CandidateServiceClient candidateServiceClient;
    private final RequisitionServiceClient requisitionServiceClient;

    /**
     * Accept offer and trigger employee creation workflow
     */
    @Transactional
    public Offer acceptOffer(String offerId, LocalDate acceptanceDate) {
        log.info("Processing offer acceptance for offer: {}", offerId);

        // Get offer details
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("Offer not found: " + offerId));

        if (offer.getStatus() != OfferStatus.EXTENDED) {
            throw new IllegalStateException("Only EXTENDED offers can be accepted");
        }

        // Update offer status
        offer.setStatus(OfferStatus.ACCEPTED);
        offer.setAcceptanceDate(acceptanceDate);
        offer.setRespondedDate(LocalDate.now());
        offer = offerRepository.save(offer);

        // Get candidate details from Candidate Service
        var candidateData = candidateServiceClient.getCandidateById(offer.getCandidateId());
        
        // Get requisition details from Requisition Service
        var requisitionData = requisitionServiceClient.getRequisitionById(offer.getRequisitionId());

        // Create and publish Offer Accepted Event
        TalentOfferAcceptedEvent event = new TalentOfferAcceptedEvent(
            offer.getOrganizationId(),
            offer.getCreatedBy(),
            "offer-service",
            offer.getOfferId(),
            offer.getCandidateId(),
            offer.getRequisitionId()
        );

        // Populate event with complete data for employee creation
        event.setJobId(offer.getJobId());
        event.setCandidateEmail(candidateData.getEmail());
        event.setCandidateFirstName(candidateData.getFirstName());
        event.setCandidateLastName(candidateData.getLastName());
        event.setCandidatePhone(candidateData.getPhone());
        event.setStartDate(offer.getStartDate());
        event.setPosition(requisitionData.getJobTitle());
        event.setDepartment(requisitionData.getDepartment());
        event.setReportingManagerId(requisitionData.getHiringManagerId());
        event.setSalary(offer.getBaseSalary());
        event.setEmploymentType(requisitionData.getEmploymentType());
        event.setLocation(requisitionData.getLocation());
        
        // Set comprehensive candidate data
        event.setCandidateData(Map.of(
            "education", candidateData.getHighestDegree(),
            "experience", candidateData.getYearsOfExperience(),
            "skills", candidateData.getSkills(),
            "resumeUrl", candidateData.getResumeUrl(),
            "linkedInUrl", candidateData.getLinkedInUrl()
        ));
        
        // Set offer details
        event.setOfferDetails(Map.of(
            "baseSalary", offer.getBaseSalary(),
            "bonus", offer.getBonus(),
            "equity", offer.getEquityGrant(),
            "benefits", offer.getBenefits(),
            "offerLetterUrl", offer.getOfferLetterUrl()
        ));

        // Publish event - this triggers employee creation in Employee Service
        eventPublisher.publishEvent("talent.offer.accepted", event);

        log.info("Offer accepted event published for offer: {}. Employee creation workflow initiated.", offerId);

        return offer;
    }

    /**
     * Reject offer
     */
    @Transactional
    public Offer rejectOffer(String offerId, String rejectionReason) {
        log.info("Processing offer rejection for offer: {}", offerId);

        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("Offer not found: " + offerId));

        offer.setStatus(OfferStatus.REJECTED);
        offer.setRejectionReason(rejectionReason);
        offer.setRespondedDate(LocalDate.now());
        offer = offerRepository.save(offer);

        // Publish rejection event
        eventPublisher.publishEvent("talent.offer.rejected", createOfferEvent(offer));

        log.info("Offer rejected: {}", offerId);

        return offer;
    }

    /**
     * Create offer for candidate
     */
    @Transactional
    public Offer createOffer(CreateOfferRequest request) {
        log.info("Creating offer for candidate: {}", request.getCandidateId());

        Offer offer = Offer.builder()
            .offerId(UUID.randomUUID().toString())
            .organizationId(request.getOrganizationId())
            .candidateId(request.getCandidateId())
            .requisitionId(request.getRequisitionId())
            .jobId(request.getJobId())
            .baseSalary(request.getBaseSalary())
            .bonus(request.getBonus())
            .equityGrant(request.getEquityGrant())
            .benefits(request.getBenefits())
            .startDate(request.getStartDate())
            .expiryDate(request.getExpiryDate())
            .status(OfferStatus.DRAFT)
            .createdBy(request.getCreatedBy())
            .createdDate(LocalDate.now())
            .build();

        offer = offerRepository.save(offer);

        log.info("Offer created: {}", offer.getOfferId());

        return offer;
    }

    /**
     * Send/extend offer to candidate
     */
    @Transactional
    public Offer extendOffer(String offerId) {
        log.info("Extending offer: {}", offerId);

        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("Offer not found: " + offerId));

        offer.setStatus(OfferStatus.EXTENDED);
        offer.setExtendedDate(LocalDate.now());
        offer = offerRepository.save(offer);

        // Publish offer extended event
        eventPublisher.publishEvent("talent.offer.made", createOfferEvent(offer));

        log.info("Offer extended: {}", offerId);

        return offer;
    }

    private Map<String, Object> createOfferEvent(Offer offer) {
        return Map.of(
            "offerId", offer.getOfferId(),
            "candidateId", offer.getCandidateId(),
            "requisitionId", offer.getRequisitionId(),
            "jobId", offer.getJobId(),
            "status", offer.getStatus(),
            "baseSalary", offer.getBaseSalary(),
            "startDate", offer.getStartDate()
        );
    }
}

