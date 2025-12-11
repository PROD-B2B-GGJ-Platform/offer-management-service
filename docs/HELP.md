# Offer Management Service - Help Guide

## Quick Start

```bash
git clone https://github.com/PROD-B2B-GGJ-Platform/offer-management-service.git
cd offer-management-service

export DB_HOST=localhost
mvn spring-boot:run
```

Access: http://localhost:8096/swagger-ui.html

---

## API Examples

### Create Offer

```bash
curl -X POST http://localhost:8096/api/v1/offers \
  -H "Content-Type: application/json" \
  -d '{
    "applicationId": "app-uuid",
    "baseSalary": 150000,
    "currency": "USD",
    "bonusAmount": 20000,
    "equityPercentage": 0.1,
    "startDate": "2025-02-01",
    "expiresAt": "2025-12-25T00:00:00Z"
  }'
```

### Send Offer

```bash
curl -X POST http://localhost:8096/api/v1/offers/{id}/send
```

### Accept Offer (Candidate)

```bash
curl -X POST http://localhost:8096/api/v1/offers/{id}/accept \
  -H "Content-Type: application/json" \
  -d '{
    "signatureData": "base64-signature"
  }'
```

---

## Offer Status Flow

```
DRAFT → PENDING_APPROVAL → APPROVED → SENT → ACCEPTED/DECLINED
```

---

## Support

GitHub: https://github.com/PROD-B2B-GGJ-Platform/offer-management-service

