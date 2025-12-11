# Offer Management Service - Functional Design Specification (FDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |

---

## 1. Functional Requirements

### FR-001: Create Offer

Create job offer for selected candidate.

**Input:**
- applicationId (required)
- baseSalary, currency
- bonusAmount, equityPercentage
- startDate
- expiresAt
- templateId

**Business Rules:**
- Application must be in OFFER stage
- Offer starts in DRAFT status
- Compensation within job band limits

---

### FR-002: Approve Offer

Manager/HR approves offer before sending.

**Business Rules:**
- Multi-level approval for high compensation
- Approval required before sending
- Audit trail of approvals

---

### FR-003: Send Offer

Send approved offer to candidate.

**Actions:**
- Generate PDF from template
- Email to candidate
- Start expiration timer
- Update status to SENT

---

### FR-004: Accept/Decline Offer

Candidate responds to offer.

**Accept Flow:**
- Update status to ACCEPTED
- Trigger onboarding workflow
- Notify hiring team
- Close job (if filled)

**Decline Flow:**
- Update status to DECLINED
- Record decline reason
- Notify hiring team
- Return application to pipeline

---

## 2. Compensation Management

### 2.1 Components

| Component | Description |
|-----------|-------------|
| Base Salary | Annual fixed compensation |
| Signing Bonus | One-time bonus |
| Equity | Stock/options grant |
| Relocation | Relocation assistance |
| Benefits | Benefits package info |

---

## 3. Acceptance Criteria

- [ ] Offers created with compensation details
- [ ] Approval workflow enforced
- [ ] PDF generated from templates
- [ ] Email sent to candidates
- [ ] Accept/decline tracked
- [ ] Events published on status changes

