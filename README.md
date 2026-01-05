# 🏦 Banking System (Java)

A Java-based banking system built using **Object-Oriented Programming (OOP)** principles and a **Swing-based GUI**.  
This project demonstrates clean architecture, business logic separation, and real-world banking rules such as overdraft handling.

---

## ✨ Features

- Create customers
- Open **Savings** and **Chequing** accounts
- Deposit, withdraw, and transfer funds
- Overdraft support **(Chequing only)**
- Real-time balance & available funds display
- Activity log for all actions
- Clean layered architecture (UI, Service, Domain)

---

## 🛠️ Technologies Used

- Java
- Java Swing (GUI)
- NetBeans IDE
- Git & GitHub

---

## ▶️ Running the Project

1. Clone the repository:
```bash
git clone https://github.com/Moealra/BankingSystem.git
```

2. Open the project in **NetBeans**

3. Run:
```
BankingApplication.java
```

---

## 📂 Project Structure

```
com.moayad.bank
├── app        → Application entry point
├── domain     → Models (Account, Customer, Transaction)
├── service    → Business logic (BankService)
├── ui         → Swing UI (MainFrame)
```

---

## 📌 Banking Rules Implemented

- **Savings accounts**
  - No overdraft allowed
  - Withdrawals fail if balance is insufficient

- **Chequing accounts**
  - Support overdraft up to a defined limit
  - Available funds = balance + overdraft

- Transfers respect account rules (no overdraft from savings)

---

## 👤 Author

**Moayad Alrahahleh**
