package com.platform.talent.offer.api;

import com.platform.talent.offer.domain.model.Offer;
import com.platform.talent.offer.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<Offer> createOffer(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestBody Offer offer
    ) {
        offer.setOrganizationId(organizationId);
        Offer created = offerService.createOffer(offer);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{offerId}/accept")
    public ResponseEntity<Offer> acceptOffer(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String offerId
    ) {
        Offer accepted = offerService.acceptOffer(organizationId, offerId);
        return ResponseEntity.ok(accepted);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<Offer> getOffer(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String offerId
    ) {
        Offer offer = offerService.getOffer(organizationId, offerId);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<Offer>> getCandidateOffers(
        @PathVariable String candidateId
    ) {
        List<Offer> offers = offerService.getCandidateOffers(candidateId);
        return ResponseEntity.ok(offers);
    }
}

