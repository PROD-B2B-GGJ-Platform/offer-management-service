package com.platform.talent.offer.repository;

import com.platform.talent.offer.domain.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
    List<Offer> findByOrganizationId(String organizationId);
    Optional<Offer> findByOrganizationIdAndOfferId(String organizationId, String offerId);
    List<Offer> findByCandidateId(String candidateId);
    List<Offer> findByStatus(String status);
}

