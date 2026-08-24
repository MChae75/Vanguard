# Vanguard FinTech Dashboard

A modern, responsive financial web application designed for Vanguard's engineering ecosystem. This project demonstrates full-stack capabilities using **Spring Boot (Backend)** and **Angular (Frontend)**, focusing on robust financial calculations, dynamic data visualization, and a seamless user experience.

## Features

This application combines two core financial tools into a single, unified interface accessible via navigation tabs:

### 1. Investment Portfolio Dashboard
A comprehensive view of a user's investments, asset allocation, and market performance.
- **Asset Allocation:** Visualizes the distribution of assets (Stocks, Bonds, ETFs, Cash) using pie/doughnut charts.
- **Performance Tracking:** Displays historical performance and portfolio growth over time.
- **Real-time Data:** (Planned) Integration with external financial APIs to fetch real-time stock and ETF prices.
- **Reactive UI:** Leverages Angular and RxJS to efficiently handle data streams and UI updates.

### 2. Retirement Goal Planner
An interactive calculator and simulation tool to help users plan for their future.
- **Future Value Simulation:** Calculates projected retirement savings based on current age, retirement age, initial savings, and monthly contributions.
- **Dynamic Charting:** Instantly updates a growth chart as the user adjusts input sliders, utilizing RxJS `debounceTime` for smooth performance.
- **Financial Algorithms:** Complex compound interest and inflation-adjusted calculations processed efficiently by the Spring Boot backend.

## Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
  - Spring Web (REST APIs)
  - Spring Data JPA (Data Persistence)
  - Spring Validation
- **Database:** H2 (In-memory for development) / PostgreSQL (Production)

### Frontend
- **Angular 16+**
- **RxJS** (Reactive programming and state management)
- **Chart.js / ng2-charts** (Data visualization)
- **Angular Material** (UI Components and Styling)

## Architecture

The project is structured as a monorepo for simplicity during development (or split into distinct backend/frontend folders):
- `/backend`: Spring Boot application serving RESTful endpoints.
- `/frontend`: Angular SPA consuming the backend APIs.

## Getting Started

### Prerequisites
- **Java 17** or higher
- **Node.js** (v18.19.1+ or newer recommended)
- **PostgreSQL** running locally on port 5432. 
  - Ensure a database named `vanguard` exists.
  - Default credentials in `application.properties` are `postgres` / `postgres`.

### 1. Run the Backend (Spring Boot)
Open a terminal and navigate to the `backend` directory:
```bash
cd backend
./mvnw spring-boot:run
```
The backend REST API will start on `http://localhost:8081`.

### 2. Run the Frontend (Angular)
Open a new terminal and navigate to the `frontend` directory:
```bash
cd frontend
npm install
npm start
```
The Vanguard dashboard will be accessible at `http://localhost:4200`.
