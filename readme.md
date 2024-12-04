## Login Credentials

The system comes with pre-initialized user accounts for testing purposes:

| Role               | Username   | Default Role         |
|--------------------|------------|----------------------|
| Administrator      | admin      | ADMIN                |
| Doctor            | doctor     | DOCTOR               |
| Patient           | patient    | PATIENT              |
| Dietician         | dietician  | DIETICIAN            |
| Pharmacist        | pharmacist | PHARMACIST           |
| IT Support        | itsupport  | IT_SUPPORT           |
| Community Manager | community  | COMMUNITY_MANAGER    |

### Important Notes:
1. For development and testing purposes:
   - Any password can be used for login
   - Authentication is simplified for testing
   - User sessions are not persisted

2. Security Considerations:
   - This is a development setup only
   - In production, implement:
     - Secure password hashing
     - Password complexity requirements
     - Session management
     - Rate limiting
     - Two-factor authentication (2FA) for admin accounts

3. Role Permissions:
   - ADMIN: Full system access
   - DOCTOR: Patient management, appointments, prescriptions
   - PATIENT: View appointments, medical records, prescriptions
   - DIETICIAN: Manage diet plans and consultations
   - PHARMACIST: Manage pharmacy inventory and medication transfers
   - IT_SUPPORT: Manage support tickets and system issues
   - COMMUNITY_MANAGER: Community health metrics, vaccination drives