# 🏋️ Gym Management System

A professional Java-based desktop application designed to manage gym operations, including client tracking and workout session scheduling. This project demonstrates a clean implementation of software engineering principles, featuring a modular architecture and multiple data persistence strategies.

## 🚀 Key Features
* **Dual Interface:** Full support for both **Command Line Interface (CLI)** and a modern **JavaFX/FXML GUI**.
* **Layered Architecture:** Implements a strict separation of concerns through Domain, Service, Repository, and UI layers.
* **Flexible Persistence:** Advanced data handling with support for:
    * **Binary** (.bin) file storage.
    * **Text** (.txt) file storage.
    * **Database** integration.
* **Advanced Logic:**
    * **Undo/Redo System:** Dedicated service to manage and revert user actions.
    * **Custom Validation:** Robust data validation layer for entities.
    * **Filtering Engine:** Dynamic filtering for clients and sessions (by name, email, date, etc.).

## 📂 Project Structure
Based on the implemented packages:
* **`src/gym/domain`**: Core entities and business models (Client, Session).
* **`src/gym/repository`**: Data access abstraction layer.
* **`src/gym/service`**: Business logic orchestration.
* **`src/gym/ui`**: Controller and view logic for both CLI and JavaFX.
* **`src/gym/undo`**: Implementation of the Undo/Redo design pattern.
* **`src/gym/validator`**: Dedicated logic for input data integrity.
* **`src/gym/filter`**: Complex query and data filtering logic.

## 🛠️ Technologies Used
* **Java 17+**.
* **JavaFX & FXML** for the Graphical User Interface.
* **Object-Oriented Design Patterns** (Repository, Service, Validator).

*Developed as part of the Advanced Programming Techniques (APT) curriculum.*
