# MathLetics – Demo Video Script (Ufuture Submission)

**Team:** STARLA.2.0  
**Project:** MathLetics (CSC557 Mobile Programming)  
**Suggested video length:** 8–12 minutes

---

## Before Recording

1. Open project in Android Studio: `C:\Users\aena zam\Documents\MathLetics`
2. Run app on emulator or physical device
3. Use screen recorder (OBS, Windows Game Bar, or phone screen record)
4. Speak clearly in English or Bahasa Melayu

---

## Part 1: Introduction (30 seconds)

**[Show Login screen]**

> "Assalamualaikum / Hello. We are Team STARLA.2.0 from CSC557 Mobile Programming. Our project is **MathLetics** – a gamified mathematics learning app for primary school students. The app uses Java, Android Studio, and SQLite local database."

---

## Part 2: Login & Authentication (1 minute)

**[Login screen]**

> "This is the Login page. Users select their role – Student or Admin – then enter username and password and press GO."

**Demo:**
- Select **Student**
- Login: `izzah` / `123456`
- Show successful login toast and redirect to Student Home

> "Authentication is handled using SQLite database, following Lab 5.0 and 5.1 database connection concepts."

---

## Part 3: Student Sign Up (45 seconds)

**[Logout → Login → Sign Up]**

> "New students can register with username, email, password, and grade level. The grade level automatically maps the student to Lower Primary Year 1–3 or Upper Primary Year 4–6."

**Demo:**
- Tap SIGN UP (as Student)
- Show signup form fields
- Go back (or register a test account)

---

## Part 4: Student Homepage & Chapters (1.5 minutes)

**[Student Home – Lower or Upper Primary]**

> "After login, students see their homepage based on grade level. Chapter quests show progress bars, trophies for completed chapters, and locked chapters that unlock as they progress."

**Demo:**
- Point out welcome header with stars and coins
- Tap an unlocked chapter → start quiz
- Long-press a chapter → show context menu (Details / Start Quiz)

---

## Part 5: Quiz Gameplay (2 minutes)

**[Quiz screen]**

> "During the quiz, students answer multiple-choice questions. A progress bar shows question number, and timer mode can be enabled from Quiz Modes."

**Demo:**
- Answer 2–3 questions
- Show timer (if enabled)
- Submit and reach Quiz Result screen
- Show score, points earned, time taken

> "Scores are saved to SQLite and update the student's total points, coins, and stars."

---

## Part 6: Quiz Modes, Leaderboard & Profile (2 minutes)

**[Quiz Modes]**

> "Students can customize flashcard mode, timer mode, and timer duration before starting a quiz."

**[Leaderboard]**

> "The Global Leaderboard ranks students by total points with a podium for top 3 performers."

**[Student Profile]**

> "The Student Profile shows merit badges, learning journey progress bar, coins, and stars – our gamification reward system."

---

## Part 7: Admin Flow (2.5 minutes)

**[Logout → Login as Admin]**

- Login: `admin1` / `123456`

> "Admins have a separate flow. The Admin Homepage shows total quizzes created and students managed."

**Demo Admin Home:**
- Show quiz list with question count and average scores
- Tap Create Quiz

**[Create Quiz]**

> "Admins create quizzes by selecting grade tier, topic, and adding up to 20 questions – as specified in our project proposal flowchart."

**Demo:**
- Add 1 sample question
- Save quiz (or show existing quizzes)

**[Dashboard]**

> "The Dashboard shows student performance on quizzes created by this admin. VIEW PROGRESS opens the quiz-specific leaderboard."

**[Admin Profile]**

> "Admin Profile displays creator journey progress and achievements."

**[Delete Quiz – optional]**

> "We implemented CRUD operations – Create, Read, and Delete quizzes using RecyclerView and AlertDialog, following Lab 5.2 to 5.4."

---

## Part 8: Lab Activity Integration (1 minute)

**[Quick code tour in Android Studio – optional]**

Mention these lab techniques used:

| Lab | Feature in MathLetics |
|-----|----------------------|
| Lab 1 – Layouts | LinearLayout, ConstraintLayout, Relative positioning |
| Lab 2 – Activities | Intent passing (quiz ID, scores, user data) |
| Lab 3 – ListView | Chapter list, Leaderboard, Dashboard adapters |
| Lab 4 – Menu | Options menu, context menu, AlertDialog |
| Lab 5.0–5.1 | SQLite authentication |
| Lab 5.2–5.4 | SQLite CRUD for users, quizzes, results |

---

## Part 9: Database & Conclusion (45 seconds)

**[Show DatabaseHelper or README ERD section]**

> "Our SQLite database has six tables: users, quizzes, questions, quiz_results, achievements, and chapter_progress – matching our project ERD."

**[Closing on Login or Home screen]**

> "MathLetics transforms primary math into a fun, gamified adventure. Thank you – Team STARLA.2.0."

---

## Upload Checklist

- [ ] Record full demo following script above
- [ ] Upload video to Google Drive
- [ ] Set sharing to "Anyone with the link"
- [ ] Paste Google Drive link on Ufuture
- [ ] Include screenshots of key screens in report if required

## Test Accounts Quick Reference

| Role | Username | Password |
|------|----------|----------|
| Admin | admin1 | 123456 |
| Student Y2 | izzah | 123456 |
| Student Y5 | aina | 123456 |
