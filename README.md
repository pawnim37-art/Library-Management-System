# 📚 Library Management System

A full-stack, enterprise-grade **Library Management System** built with **Spring Boot 3 (Java 25)** on the backend and **React + Vite + Tailwind CSS** on the frontend. Features complete book catalog management, real-time circulation tracking (borrowing, returns, renewals, reservations), automated fine calculation in Indian Rupee (₹), JWT-based role authorization, and downloadable PDF/CSV reports.

---

## 🌟 Key Features

### 📖 Catalog Management
- Full CRUD operations for Books, Authors, and Categories.
- Search and filter catalog by title, author, category, ISBN, or availability.
- Track physical copies, barcodes, publication details, and book condition.

### 🔄 Book Circulation & Borrowing
- **Member Self-Borrowing**: Library members can directly check out available books.
- **Librarian Desk**: Issue and process book returns with barcode tracking.
- **Renewals & Holds**: Members can renew active loans or place hold reservations.
- **Automatic Stock Updates**: Real-time management of total and available book copies.

### 💰 Fine & Overdue Management
- **Automatic Fine Calculation**: Accrues overdue fines per day (in **Indian Rupee - ₹**).
- **Payment & Waiver**: Process fine payments or record librarian waivers.
- **Fine Audit Log**: Detailed records of fine status (`PENDING`, `PAID`, `WAIVED`).

### 🔒 Security & User Roles
- **Role-Based Access Control (RBAC)**:
  - `ROLE_ADMIN`: System-wide access, user management, catalog management, fine management, reports.
  - `ROLE_LIBRARIAN`: Catalog desk, issue/return management, fine processing/waivers, reports.
  - `ROLE_MEMBER`: Browse catalog, borrow books, view active loans, reservations, pay fines.
- **Stateless Authentication**: Secured via JWT (JSON Web Tokens) and BCrypt password hashing.

### 📊 Reports & Analytics
- Visual dashboard statistics (total books, active loans, pending fines, top borrowed titles).
- PDF export for catalog inventory and active loans.
- CSV export for overdue fines and circulation records.

---

## 🛠️ Technology Stack

### Backend
- **Framework**: Java 25 / Spring Boot 3.3.5
- **Security**: Spring Security, JWT (JJWT)
- **Database**: H2 In-Memory Database (Spring Data JPA / Hibernate)
- **Reporting**: Apache Commons CSV, OpenPDF (iText)
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18 (Vite 8)
- **Styling**: Tailwind CSS (Glassmorphism design system)
- **Icons**: Lucide React
- **HTTP Client**: Axios

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK 17+)** installed.
- **Node.js (v18+)** and `npm` installed.

---

### 1. Backend Setup (Spring Boot)

1. Open terminal in the `backend/` directory:
   ```bash
   cd backend
   ```
2. Run the application:
   ```bash
   # On Windows (PowerShell/CMD):
   .\mvnw.cmd spring-boot:run

   # On Linux/macOS:
   ./mvnw spring-boot:run
   ```
3. The backend server will start at `http://localhost:8081`.
   - **H2 Console**: `http://localhost:8081/h2-console`
   - **JDBC URL**: `jdbc:h2:mem:librarydb` (User: `SA`, Password: empty)

---

### 2. Frontend Setup (React + Vite)

1. Open terminal in the `frontend/` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. Access the web app at `http://localhost:3000/`.

---

## 🔑 Pre-Seeded Default Accounts

| Role | Email | Password |
| :--- | :--- | :--- |
| **System Administrator** | `admin@library.com` | `admin123` |
| **Librarian** | `librarian@library.com` | `librarian123` |
| **Library Member** | `member@library.com` | `member123` |
| **Library Member 2** | `alice@library.com` | `member123` |

---

## 📁 Repository Structure

```
Library-Management-System/
├── backend/
│   ├── src/main/java/com/library/backend/
│   │   ├── config/          # Security, JWT & CORS configuration
│   │   ├── controller/      # REST API Controllers (Auth, Book, Circulation, Fine, Report)
│   │   ├── dto/             # Request & Response Data Transfer Objects
│   │   ├── entity/          # JPA Entities (User, Book, Transaction, Fine, etc.)
│   │   ├── repository/      # Spring Data Repositories
│   │   ├── service/         # Business Logic Services
│   │   └── util/            # DataSeeder & Utilities
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/      # UI Layout & Modal components
│   │   ├── pages/           # Application views (Catalog, Fines, MyLoans, Dashboard, etc.)
│   │   ├── services/        # Axios API Client & Services
│   │   └── App.jsx
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
└── README.md
```

---

## 📜 License
This project is open source under the MIT License.
