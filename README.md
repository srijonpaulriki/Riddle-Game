# 🧩 RIDDLE QUEST
### A Java-Based Console Riddle Adventure Game

**Riddle Quest** is an interactive, console-based riddle-solving game developed using **Java**. The project demonstrates Object-Oriented Programming (OOP), multithreading, exception handling, and file handling through an engaging gaming experience.

Players must solve challenging riddles, complete levels, earn points, and compete for a position on the leaderboard.

---

## 🎮 Game Overview

Riddle Quest is designed to test players' logical thinking, problem-solving abilities, and creativity.

The game provides an interactive terminal interface with colorful text, engaging challenges, and a scoring system.

Players progress through different levels by answering riddles correctly while managing their available lives and time.

## ✨ Key Features

- 🧩 **Challenging Riddles:** Solve different riddles across multiple levels.
- ❤️ **Life System:** Players have limited chances to complete challenges.
- ⏳ **Timed Challenges:** Answer riddles within the given time.
- 🏆 **Leaderboard:** Track and compare player scores.
- ⭐ **Scoring System:** Earn points by solving riddles.
- 🎨 **Colorful Console Interface:** ANSI escape codes provide an enhanced terminal experience.
- 🔊 **Sound Effects:** Audio feedback enhances gameplay.
- 👑 **Boss Levels:** Special challenges increase the difficulty.
- 💡 **Hints:** Get assistance when solving difficult riddles.
- 🔐 **Secret Features:** Discover hidden elements during gameplay.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core game development |
| Object-Oriented Programming | Game architecture |
| Multithreading | Timer and input management |
| Exception Handling | Error management |
| File Handling | Data storage |
| Java Collections | Managing game data |
| ANSI Escape Codes | Console styling |
| Windows Batch Script | Simplified game launching |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control and collaboration |

---

## 🧠 Java Concepts Implemented

The project demonstrates several important Java programming concepts.

### 1. Object-Oriented Programming (OOP)

- Classes and Objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Interfaces
- Constructors

### 2. Multithreading

- Thread creation
- Runnable interface
- Concurrent execution
- Timed input handling
- Thread synchronization and management

### 3. Exception Handling

- Try-catch blocks
- InterruptedException
- Input error handling
- File error handling

### 4. Java Collections

- ArrayList
- Queue
- Collection-based data management

### 5. File Handling

- Reading data from files
- Writing game information
- Maintaining leaderboard records

---

## 🚀 How to Run the Game

Riddle Quest can be launched using the provided **`rungame.bat`** file or directly through a Java-compatible IDE.

For Windows users, running the game through `rungame.bat` is the recommended method.

### 📋 Prerequisites

Before running the project, make sure you have:

- **Java Development Kit (JDK)** installed.
- **Windows 10/11** for the batch-file method.
- **Command Prompt, PowerShell, or Windows Terminal**.
- **IntelliJ IDEA** or another Java-compatible IDE (optional).
- **Git** (optional, for cloning the repository).

You can verify that Java is installed by opening a terminal and executing:

```bash
java -version
```

To verify that the Java compiler is available:

```bash
javac -version
```

If both commands display version information, your Java environment is ready.

### 📥 Step 1: Download the Project

You can download the project using either method.

**Option 1: Clone Using Git**

Open a terminal and execute:

```bash
git clone https://github.com/srijonpaulriki/Riddle-Game.git
```

Navigate to the downloaded project:

```bash
cd Riddle-Game
```

**Option 2: Download ZIP**

1. Visit the [Riddle Quest GitHub Repository](https://github.com/srijonpaulriki/Riddle-Game).
2. Click the green **Code** button.
3. Select **Download ZIP**.
4. Extract the downloaded ZIP file.
5. Open the extracted project folder.

### 🎮 Step 2: Launch Using `rungame.bat` (Recommended)

The project includes a Windows batch file named **`rungame.bat`**, which provides a convenient way to launch the game.

You do not need to open IntelliJ IDEA to use this method.

#### Method 1: Double-Click to Play

1. Open the downloaded project folder.
2. Locate the **`rungame.bat`** file.
3. Double-click the file.
4. Follow the instructions displayed in the terminal.
5. Enter your player name and start playing!

#### Method 2: Run Using Command Prompt

Open Command Prompt and navigate to the project directory.

Execute:

```bat
rungame.bat
```

#### Method 3: Run Using PowerShell

Open PowerShell inside the project folder.

Execute:

```powershell
.\rungame.bat
```

The batch file provides a convenient way to launch Riddle Quest without manually entering the Java launch commands.

> **Note:** The `rungame.bat` file is designed for Windows. Make sure the JDK is installed and Java is accessible through your system's PATH.

### 💻 Step 3: Run Using IntelliJ IDEA (Alternative)

Developers who want to explore or modify the source code can run the project directly through IntelliJ IDEA.

1. Launch **IntelliJ IDEA**.
2. Select **Open**.
3. Navigate to the downloaded project folder.
4. Open the project.
5. Configure the Java SDK if necessary.
6. Locate the `RiddleGame.java` file.
7. Run its `main()` method.

### 🎨 Recommended Terminal Settings

For the best gameplay experience:

- Use **Windows Terminal, Command Prompt, or PowerShell**.
- Use a terminal that supports ANSI escape codes.
- Enable UTF-8 support for Unicode characters.
- Maximize the terminal window for better visibility.
- Ensure system audio is enabled for sound effects.

**🎉 You're ready to begin your Riddle Quest adventure!**

---

## 🎯 How to Play

1. Launch Riddle Quest using `rungame.bat` or IntelliJ IDEA.
2. Enter your player name.
3. Start the game.
4. Read each riddle carefully.
5. Type your answer and press Enter.
6. Solve riddles to earn points.
7. Manage your remaining lives and available time.
8. Use hints when you need assistance.
9. Complete levels and overcome boss challenges.
10. Try to achieve the highest score on the leaderboard.

### 🏆 Your Mission

**Solve riddles, survive challenges, earn points, and become the ultimate Riddle Quest champion!**

---

## 🏗️ Project Architecture

Riddle Quest follows an Object-Oriented Programming approach.

The project is organized into multiple classes, each responsible for specific game functionalities.

### Main Components

#### 🧩 RiddleGame

- Contains the `main()` method.
- Serves as the entry point of the application.
- Initializes and starts the game engine.

#### ⚙️ GameEngine

- Controls the overall gameplay.
- Manages game progression.
- Handles player interactions.
- Coordinates different game components.

#### 📚 Level

- Represents the base structure of game levels.
- Defines common properties and behaviors.
- Supports inheritance and polymorphism.
- Provides shared functionality for different level types.

#### ⌨️ InputReader

- Handles player input.
- Uses the `Runnable` interface.
- Supports concurrent input processing.
- Helps manage input during timed challenges.

#### 🎨 ConsoleUI

- Manages terminal output.
- Displays colorful text.
- Handles game messages and visual elements.
- Improves the overall console experience.

#### 🏆 Leaderboard

- Manages player rankings.
- Stores and retrieves player scores.
- Displays leaderboard information.
- Maintains player performance records.

### Supporting Components

Other supporting classes handle:

- Riddle data management
- Player information
- Scoring calculations
- Sound effects
- Game levels and boss challenges
- Additional gameplay functionality

This modular architecture improves code organization, readability, maintainability, and reusability.

---

## 👨‍💻 Development Team

Riddle Quest was collaboratively developed by three team members.

| Team Member | Role |
|---|---|
| **Riad** | Game Development |
| **Srijon Paul** | Game Development |
| **Akash** | Game Development |

All three members contributed to the development, testing, and improvement of the project.

### 🤝 Collaboration

Git and GitHub were used for:

- Version control
- Code sharing
- Collaborative development
- Branch management
- Tracking changes
- Integrating team contributions

This collaborative workflow helped the team gain practical experience with software development and project coordination.

---

## 🎓 Project Purpose

Riddle Quest was developed as an academic Java programming project.

The primary objectives are to:

- Apply Object-Oriented Programming principles.
- Implement multithreading in a practical application.
- Demonstrate exception handling.
- Practice file handling and data management.
- Utilize Java Collections.
- Develop a modular and maintainable application.
- Improve logical thinking and problem-solving skills.
- Gain practical experience with Git and GitHub.
- Develop teamwork and collaborative programming skills.

The project combines theoretical Java programming concepts with practical game development.

---

## 🔮 Future Improvements

Although Riddle Quest is designed as a console-based game, several enhancements could be introduced in future versions.

Potential improvements include:

- 🖥️ **Graphical User Interface:** Develop a GUI using JavaFX or Swing.
- 🧩 **Additional Riddles:** Introduce more riddles and categories.
- 👑 **Advanced Boss Levels:** Add more complex challenges.
- 👥 **Multiplayer Mode:** Allow multiple players to compete.
- 🌐 **Online Leaderboard:** Store and compare scores online.
- 🎵 **Enhanced Audio:** Introduce background music and improved sound effects.
- 🎬 **Improved Animations:** Add more engaging visual effects.
- ⚙️ **Difficulty Settings:** Allow players to select difficulty levels.
- 💾 **Save System:** Enable players to save and resume progress.
- 🏅 **Achievements:** Reward players for completing special challenges.

---

## 📜 License

This project was developed for educational purposes.

---

## ⭐ Support the Project

If you find Riddle Quest interesting, consider supporting the project by giving the repository a ⭐ on GitHub.

Your support encourages further development and improvements!

**🔗 GitHub Repository:** [Riddle Quest](https://github.com/srijonpaulriki/Riddle-Game)

---

### 🧩 Think Smart. Solve Fast. Conquer the Quest!

**Made with Java ☕ | Riddle Quest 🧩**

**Developed by Riad, Srijon Paul & Akash Talukder**
