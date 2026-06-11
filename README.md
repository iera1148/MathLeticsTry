# MathLetics - CSC557 Mobile Programming Project

**Team:** STARLA.2.0  
**Theme:** Gamified Learning  
**Lecturer:** Ts. Nur Hasni Nasruddin

## Project Overview

MathLetics is a gamified primary school mathematics learning app with a sketchbook/notebook UI aesthetic. It supports two user roles (Student and Admin) with separate flows as defined in the project proposal.

## Tech Stack

- **IDE:** Android Studio
- **Language:** Java
- **Database:** SQLite (local, offline)
- **Min SDK:** 24 | **Target SDK:** 34

## Features (Matching Project Proposal)

### Authentication
- Login with Student/Admin role selection
- Student signup (username, email, password, grade level)
- Admin signup (organization, username, email, position, password)

### Student Module
- Lower Primary (Year 1-3) / Upper Primary (Year 4-6) homepage
- Chapter quest selection with progress bars and locked chapters
- In-game quiz with timer, progress bar, and scoring
- Global leaderboard with podium display
- Quiz modes (flashcard, timer, duration customization)
- Student profile with badges, coins, stars, journey progress

### Admin Module
- Admin homepage with quiz stats and student count
- Create quiz (max 20 questions, grade tier selection)
- Dashboard showing student quiz performance
- View progress → quiz-specific leaderboard
- Admin profile with creator achievements
- CRUD: Create, Read, Delete quizzes (RecyclerView + AlertDialog)

## Lab Activity Integration (CSC557 Rubric)

| Lab Topic | Implementation |
|-----------|----------------|
| Layout Design (Linear, Relative, Constraint) | Used across all activity layouts |
| Activity Lifecycle | LoginActivity logs full lifecycle |
| Passing Values (Intent) | Quiz ID, scores, user data between activities |
| ListView + Custom Adapter | Chapters, Leaderboard, Dashboard, Badges |
| RecyclerView + Adapter | Admin quiz list with Update/Delete |
| Options Menu | Student & Admin home screens |
| Alert Dialog | Report bugs, delete confirmation |
| SQLite Auth | Login/signup with local database |
| SQLite CRUD | Users, quizzes, questions, results, achievements |

## Database Schema (ERD)

- **users** - students and admins
- **quizzes** - admin-created quiz sets
- **questions** - up to 20 per quiz
- **quiz_results** - student scores and attempts
- **achievements** - merit badges
- **chapter_progress** - quest completion tracking

## Default Test Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin1 | 123456 |
| Student (Year 2) | izzah | 123456 |
| Student (Year 5) | aina | 123456 |
| Student (Year 3) | sharmiza | 123456 |

## How to Run

1. Open Android Studio
2. **File → Open** → select this `MathLetics` folder
3. Update `local.properties` with your Android SDK path if needed
4. Sync Gradle and run on emulator or device

## Demo Video for Submission

See **[DEMO_VIDEO_SCRIPT.md](DEMO_VIDEO_SCRIPT.md)** for a full step-by-step recording script (8–12 min) covering student flow, admin flow, lab integration, and Ufuture upload checklist.

## Build from Command Line (optional)

```bat
cd "C:\Users\aena zam\Documents\MathLetics"
gradlew.bat assembleDebug
```

APK output: `app\build\outputs\apk\debug\app-debug.apk`

## Team Members

- Nurul Izzah Atirah Binti Ahmad Saidin (2025173011)
- Nur Aina Natasha Binti Rusliezan (2025152725)
- Sharmiza Binti Saharudin (2025168897)
- Nuraliah Atikah Binti Azlan (2025301431)
