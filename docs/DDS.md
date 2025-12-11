# Offer Management Service - Design Document Specification (DDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |
| Owner | Talent & Recruitment Team |

---

## 1. Overview

The Offer Management Service handles the complete lifecycle of job offers, from generation through acceptance or rejection.

### 1.1 Key Features

- Offer letter generation from templates
- Digital offer delivery
- Offer tracking and status management
- E-signature integration
- Compensation package management

---

## 2. Architecture

### 2.1 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java | 21+ |
| Framework | Spring Boot | 3.2.0 |
| Database | PostgreSQL | 15+ |
| Port | 8096 | |
| Document Generation | Apache FreeMarker | 2.3.x |

---

## 3. Data Model

### 3.1 Database Schema

#### Table: offers

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| organization_id | UUID | Multi-tenant isolation |
| application_id | UUID | FK to applications |
| candidate_id | UUID | FK to candidates |
| job_id | UUID | FK to jobs |
| template_id | UUID | FK to offer_templates |
| status | VARCHAR(20) | DRAFT, PENDING_APPROVAL, APPROVED, SENT, ACCEPTED, DECLINED, EXPIRED |
| base_salary | DECIMAL(15,2) | Base salary |
| currency | VARCHAR(3) | Currency code |
| bonus_amount | DECIMAL(15,2) | Signing bonus |
| equity_percentage | DECIMAL(5,2) | Equity grant % |
| start_date | DATE | Proposed start date |
| expires_at | TIMESTAMP | Offer expiration |
| signed_at | TIMESTAMP | When signed |
| document_url | VARCHAR(500) | Signed document |
| created_at | TIMESTAMP | Creation time |

#### Table: offer_templates

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| organization_id | UUID | Multi-tenant |
| name | VARCHAR(255) | Template name |
| content | TEXT | Template content (HTML/FreeMarker) |
| is_default | BOOLEAN | Default template |

---

## 4. API Design

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/offers` | Create offer |
| GET | `/api/v1/offers/{id}` | Get offer |
| PUT | `/api/v1/offers/{id}` | Update offer |
| POST | `/api/v1/offers/{id}/approve` | Approve offer |
| POST | `/api/v1/offers/{id}/send` | Send to candidate |
| POST | `/api/v1/offers/{id}/accept` | Candidate accepts |
| POST | `/api/v1/offers/{id}/decline` | Candidate declines |
| GET | `/api/v1/offers/{id}/document` | Download offer doc |

---

## 5. Offer Workflow

```
DRAFT → PENDING_APPROVAL → APPROVED → SENT → ACCEPTED/DECLINED/EXPIRED
```

---

## 6. Document Generation

### 6.1 Template Variables

```
${candidate.fullName}
${offer.baseSalary}
${offer.currency}
${offer.startDate}
${company.name}
${job.title}
${job.department}
```

### 6.2 Output Formats

- PDF (primary)
- DOCX (editable)
- HTML (preview)

---

## 7. Events Published

| Topic | Trigger |
|-------|---------|
| talent.offer.created | Offer created |
| talent.offer.sent | Sent to candidate |
| talent.offer.accepted | Candidate accepted |
| talent.offer.declined | Candidate declined |

---

## 8. References

- [Apache FreeMarker](https://freemarker.apache.org/)
- [DocuSign API](https://developers.docusign.com/)

