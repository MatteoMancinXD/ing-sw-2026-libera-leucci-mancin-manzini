# 🏛️ Mesos - Digital Board Game

![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF8000?style=for-the-badge&logo=java&logoColor=white)

> **Academic Project - Politecnico di Milano**  
> Course: *Software Engineering - final project*  
> Evaluation: **30/30**

This repository contains the digital implementation of the board game **Mesos** (originally published by *Cranio Creations*).
The project features a fully distributed multiplayer architecture, supporting both RMI and TCP Socket connections, with data persistence for global leaderboards.

---

### ⚠️ Important Notice Regarding Graphical Assets (Copyright)
Due to strict copyright restrictions enforced by the original publisher, **the graphical assets (images, cards, totems, board) required to run the JavaFX GUI cannot be uploaded to this public repository**. 
However, the game logic is completely independent of the graphical interface. You can fully experience and play the game without any issues by selecting the **Text-User Interface (TUI)** upon launching the client.

---

## ✨ Features
* **Full Ruleset Implementation:** Complete adherence to the original Mesos board game mechanics (2 to 5 players).
* **Distributed Architecture:** Playable over a network with a centralized server managing multiple game lobbies concurrently.
* **Dual Network Protocol:** Clients can dynamically choose to connect via **TCP Sockets** or **RMI** (Remote Method Invocation).
* **Dual User Interface:** Playable via an interactive Command Line Interface (TUI) or a Graphical User Interface (GUI) built with JavaFX (assets required locally).
* **Persistent Leaderboard:** Global historical rankings stored via a connected Database.

## 🏗️ Architecture & Design Patterns
The project was designed with a strong focus on modularity, decoupling, and robustness, applying several enterprise-level design patterns:

* **Model-View-Controller (MVC):** Strict separation of concerns between game logic, user interfaces, and network controllers.
* **Virtual View Pattern:** Used to seamlessly decouple the network layer from the application logic. The server communicates with clients through a "virtual" representation of the view, oblivious to the underlying network protocol (Socket or RMI) being used.
* **Singleton:** Applied to the `DatabaseManagerDAO` to guarantee a single, thread-safe connection pool to the database across the entire server lifecycle.

