package com.starla.mathletics.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.starla.mathletics.models.Achievement;
import com.starla.mathletics.models.ChapterProgress;
import com.starla.mathletics.models.LeaderboardEntry;
import com.starla.mathletics.models.Question;
import com.starla.mathletics.models.Quiz;
import com.starla.mathletics.models.QuizResult;
import com.starla.mathletics.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mathletics.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_QUIZ_RESULTS = "quiz_results";
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String TABLE_CHAPTER_PROGRESS = "chapter_progress";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "grade_level INTEGER DEFAULT 0, " +
                "organization_name TEXT, " +
                "position TEXT, " +
                "total_points INTEGER DEFAULT 0, " +
                "coins INTEGER DEFAULT 0, " +
                "stars INTEGER DEFAULT 0, " +
                "created_at TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_QUIZZES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "admin_id INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "topic TEXT NOT NULL, " +
                "grade_tier TEXT NOT NULL, " +
                "grade_level INTEGER NOT NULL, " +
                "created_at TEXT, " +
                "FOREIGN KEY(admin_id) REFERENCES " + TABLE_USERS + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quiz_id INTEGER NOT NULL, " +
                "question_text TEXT NOT NULL, " +
                "option_a TEXT NOT NULL, " +
                "option_b TEXT NOT NULL, " +
                "option_c TEXT NOT NULL, " +
                "option_d TEXT NOT NULL, " +
                "correct_answer TEXT NOT NULL, " +
                "order_num INTEGER NOT NULL, " +
                "FOREIGN KEY(quiz_id) REFERENCES " + TABLE_QUIZZES + "(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_QUIZ_RESULTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "quiz_id INTEGER NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "total_questions INTEGER NOT NULL, " +
                "time_taken INTEGER DEFAULT 0, " +
                "timer_mode INTEGER DEFAULT 0, " +
                "flashcard_mode INTEGER DEFAULT 0, " +
                "completed_at TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES " + TABLE_USERS + "(id), " +
                "FOREIGN KEY(quiz_id) REFERENCES " + TABLE_QUIZZES + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_ACHIEVEMENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "badge_name TEXT NOT NULL, " +
                "earned_at TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES " + TABLE_USERS + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_CHAPTER_PROGRESS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "chapter_name TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "progress_percent INTEGER DEFAULT 0, " +
                "FOREIGN KEY(student_id) REFERENCES " + TABLE_USERS + "(id))");

        seedDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTER_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedDefaultData(SQLiteDatabase db) {
        String now = currentTimestamp();

        ContentValues admin = new ContentValues();
        admin.put("username", "admin1");
        admin.put("email", "admin@mathletics.edu");
        admin.put("password", "123456");
        admin.put("role", "admin");
        admin.put("organization_name", "STARLA Academy");
        admin.put("position", "Math Coordinator");
        admin.put("created_at", now);
        long adminId = db.insert(TABLE_USERS, null, admin);

        ContentValues student1 = new ContentValues();
        student1.put("username", "izzah");
        student1.put("email", "izzah@student.edu");
        student1.put("password", "123456");
        student1.put("role", "student");
        student1.put("grade_level", 2);
        student1.put("total_points", 150);
        student1.put("coins", 50);
        student1.put("stars", 12);
        student1.put("created_at", now);
        long student1Id = db.insert(TABLE_USERS, null, student1);

        ContentValues student2 = new ContentValues();
        student2.put("username", "aina");
        student2.put("email", "aina@student.edu");
        student2.put("password", "123456");
        student2.put("role", "student");
        student2.put("grade_level", 5);
        student2.put("total_points", 320);
        student2.put("coins", 80);
        student2.put("stars", 25);
        student2.put("created_at", now);
        long student2Id = db.insert(TABLE_USERS, null, student2);

        ContentValues student3 = new ContentValues();
        student3.put("username", "sharmiza");
        student3.put("email", "sharmiza@student.edu");
        student3.put("password", "123456");
        student3.put("role", "student");
        student3.put("grade_level", 3);
        student3.put("total_points", 200);
        student3.put("coins", 60);
        student3.put("stars", 18);
        student3.put("created_at", now);
        db.insert(TABLE_USERS, null, student3);

        ContentValues quiz1 = new ContentValues();
        quiz1.put("admin_id", adminId);
        quiz1.put("title", "Addition Adventure");
        quiz1.put("topic", "Numbers & Operations");
        quiz1.put("grade_tier", "Lower Primary");
        quiz1.put("grade_level", 2);
        quiz1.put("created_at", now);
        long quiz1Id = db.insert(TABLE_QUIZZES, null, quiz1);

        insertQuestion(db, (int) quiz1Id, "What is 5 + 3?", "6", "7", "8", "9", "C", 1);
        insertQuestion(db, (int) quiz1Id, "What is 12 + 4?", "14", "15", "16", "17", "C", 2);
        insertQuestion(db, (int) quiz1Id, "What is 9 + 1?", "8", "9", "10", "11", "C", 3);

        ContentValues quiz2 = new ContentValues();
        quiz2.put("admin_id", adminId);
        quiz2.put("title", "Fractions Hero");
        quiz2.put("topic", "Relations & Algebra");
        quiz2.put("grade_tier", "Upper Primary");
        quiz2.put("grade_level", 5);
        quiz2.put("created_at", now);
        long quiz2Id = db.insert(TABLE_QUIZZES, null, quiz2);

        insertQuestion(db, (int) quiz2Id, "What is 1/2 + 1/2?", "1/4", "1", "2", "1/3", "B", 1);
        insertQuestion(db, (int) quiz2Id, "Which is greater: 3/4 or 1/2?", "3/4", "1/2", "Equal", "Cannot tell", "A", 2);

        seedChapterProgress(db, (int) student1Id, true);
        seedChapterProgress(db, (int) student2Id, false);

        ContentValues badge1 = new ContentValues();
        badge1.put("student_id", student1Id);
        badge1.put("badge_name", "Math Starter");
        badge1.put("earned_at", now);
        db.insert(TABLE_ACHIEVEMENTS, null, badge1);

        ContentValues badge2 = new ContentValues();
        badge2.put("student_id", student2Id);
        badge2.put("badge_name", "Fraction Master");
        badge2.put("earned_at", now);
        db.insert(TABLE_ACHIEVEMENTS, null, badge2);
    }

    private void seedChapterProgress(SQLiteDatabase db, int studentId, boolean lowerPrimary) {
        String[] chapters = lowerPrimary
                ? new String[]{"Numbers & Operations", "Measurement Basics", "Geometry Shapes", "Addition Adventure", "Subtraction Quest"}
                : new String[]{"Fractions Hero", "Algebra Basics", "Geometry Master", "Statistics Explorer", "Equation Challenge"};
        String[] statuses = {"completed", "in_progress", "locked", "locked", "locked"};
        int[] progress = {100, 45, 0, 0, 0};

        for (int i = 0; i < chapters.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put("student_id", studentId);
            cv.put("chapter_name", chapters[i]);
            cv.put("status", statuses[i]);
            cv.put("progress_percent", progress[i]);
            db.insert(TABLE_CHAPTER_PROGRESS, null, cv);
        }
    }

    private void insertQuestion(SQLiteDatabase db, int quizId, String text, String a, String b, String c, String d, String correct, int order) {
        ContentValues cv = new ContentValues();
        cv.put("quiz_id", quizId);
        cv.put("question_text", text);
        cv.put("option_a", a);
        cv.put("option_b", b);
        cv.put("option_c", c);
        cv.put("option_d", d);
        cv.put("correct_answer", correct);
        cv.put("order_num", order);
        db.insert(TABLE_QUESTIONS, null, cv);
    }

    private String currentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // --- USER CRUD ---

    public long insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("role", user.getRole());
        cv.put("grade_level", user.getGradeLevel());
        cv.put("organization_name", user.getOrganizationName());
        cv.put("position", user.getPosition());
        cv.put("created_at", currentTimestamp());
        return db.insert(TABLE_USERS, null, cv);
    }

    public User authenticate(String username, String password, String role) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                "username = ? AND password = ? AND role = ?",
                new String[]{username, password, role}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = mapUser(cursor);
        }
        cursor.close();
        return user;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = mapUser(cursor);
        }
        cursor.close();
        return user;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id"}, "username = ?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getStudentCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE role = 'student'", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void updateUserPoints(int userId, int points, int coins, int stars) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("total_points", points);
        cv.put("coins", coins);
        cv.put("stars", stars);
        db.update(TABLE_USERS, cv, "id = ?", new String[]{String.valueOf(userId)});
    }

    private User mapUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        user.setGradeLevel(cursor.getInt(cursor.getColumnIndexOrThrow("grade_level")));
        user.setOrganizationName(cursor.getString(cursor.getColumnIndexOrThrow("organization_name")));
        user.setPosition(cursor.getString(cursor.getColumnIndexOrThrow("position")));
        user.setTotalPoints(cursor.getInt(cursor.getColumnIndexOrThrow("total_points")));
        user.setCoins(cursor.getInt(cursor.getColumnIndexOrThrow("coins")));
        user.setStars(cursor.getInt(cursor.getColumnIndexOrThrow("stars")));
        return user;
    }

    // --- QUIZ CRUD ---

    public long insertQuiz(Quiz quiz) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("admin_id", quiz.getAdminId());
        cv.put("title", quiz.getTitle());
        cv.put("topic", quiz.getTopic());
        cv.put("grade_tier", quiz.getGradeTier());
        cv.put("grade_level", quiz.getGradeLevel());
        cv.put("created_at", currentTimestamp());
        return db.insert(TABLE_QUIZZES, null, cv);
    }

    public long insertQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("quiz_id", question.getQuizId());
        cv.put("question_text", question.getQuestionText());
        cv.put("option_a", question.getOptionA());
        cv.put("option_b", question.getOptionB());
        cv.put("option_c", question.getOptionC());
        cv.put("option_d", question.getOptionD());
        cv.put("correct_answer", question.getCorrectAnswer());
        cv.put("order_num", question.getOrderNum());
        return db.insert(TABLE_QUESTIONS, null, cv);
    }

    public List<Quiz> getQuizzesByAdmin(int adminId) {
        List<Quiz> quizzes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT q.*, COUNT(qu.id) as qcount, " +
                        "COALESCE(AVG(r.score * 100.0 / r.total_questions), 0) as avgscore, " +
                        "COALESCE(COUNT(DISTINCT r.student_id) * 100.0 / ?, 0) as completion " +
                        "FROM " + TABLE_QUIZZES + " q " +
                        "LEFT JOIN " + TABLE_QUESTIONS + " qu ON q.id = qu.quiz_id " +
                        "LEFT JOIN " + TABLE_QUIZ_RESULTS + " r ON q.id = r.quiz_id " +
                        "WHERE q.admin_id = ? GROUP BY q.id ORDER BY q.id DESC",
                new String[]{String.valueOf(Math.max(getStudentCount(), 1)), String.valueOf(adminId)});

        while (cursor.moveToNext()) {
            Quiz quiz = new Quiz();
            quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            quiz.setAdminId(cursor.getInt(cursor.getColumnIndexOrThrow("admin_id")));
            quiz.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            quiz.setTopic(cursor.getString(cursor.getColumnIndexOrThrow("topic")));
            quiz.setGradeTier(cursor.getString(cursor.getColumnIndexOrThrow("grade_tier")));
            quiz.setGradeLevel(cursor.getInt(cursor.getColumnIndexOrThrow("grade_level")));
            quiz.setQuestionCount(cursor.getInt(cursor.getColumnIndexOrThrow("qcount")));
            quiz.setAvgScore(cursor.getDouble(cursor.getColumnIndexOrThrow("avgscore")));
            quiz.setCompletionRate(cursor.getDouble(cursor.getColumnIndexOrThrow("completion")));
            quizzes.add(quiz);
        }
        cursor.close();
        return quizzes;
    }

    public List<Quiz> getQuizzesForStudent(String gradeTier, int gradeLevel) {
        List<Quiz> quizzes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZZES, null,
                "grade_tier = ? AND grade_level = ?",
                new String[]{gradeTier, String.valueOf(gradeLevel)},
                null, null, "id ASC");

        while (cursor.moveToNext()) {
            Quiz quiz = mapQuiz(cursor);
            quiz.setQuestionCount(getQuestionCount(quiz.getId()));
            quizzes.add(quiz);
        }
        cursor.close();
        return quizzes;
    }

    public Quiz getQuizById(int quizId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZZES, null, "id = ?", new String[]{String.valueOf(quizId)}, null, null, null);
        Quiz quiz = null;
        if (cursor.moveToFirst()) {
            quiz = mapQuiz(cursor);
            quiz.setQuestionCount(getQuestionCount(quizId));
        }
        cursor.close();
        return quiz;
    }

    public int getQuestionCount(int quizId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS + " WHERE quiz_id = ?", new String[]{String.valueOf(quizId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getQuizCountByAdmin(int adminId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUIZZES + " WHERE admin_id = ?", new String[]{String.valueOf(adminId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public boolean deleteQuiz(int quizId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_QUESTIONS, "quiz_id = ?", new String[]{String.valueOf(quizId)});
        db.delete(TABLE_QUIZ_RESULTS, "quiz_id = ?", new String[]{String.valueOf(quizId)});
        return db.delete(TABLE_QUIZZES, "id = ?", new String[]{String.valueOf(quizId)}) > 0;
    }

    private Quiz mapQuiz(Cursor cursor) {
        Quiz quiz = new Quiz();
        quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        quiz.setAdminId(cursor.getInt(cursor.getColumnIndexOrThrow("admin_id")));
        quiz.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        quiz.setTopic(cursor.getString(cursor.getColumnIndexOrThrow("topic")));
        quiz.setGradeTier(cursor.getString(cursor.getColumnIndexOrThrow("grade_tier")));
        quiz.setGradeLevel(cursor.getInt(cursor.getColumnIndexOrThrow("grade_level")));
        return quiz;
    }

    public List<Question> getQuestionsByQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, "quiz_id = ?",
                new String[]{String.valueOf(quizId)}, null, null, "order_num ASC");

        while (cursor.moveToNext()) {
            Question q = new Question();
            q.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            q.setQuizId(cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id")));
            q.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow("question_text")));
            q.setOptionA(cursor.getString(cursor.getColumnIndexOrThrow("option_a")));
            q.setOptionB(cursor.getString(cursor.getColumnIndexOrThrow("option_b")));
            q.setOptionC(cursor.getString(cursor.getColumnIndexOrThrow("option_c")));
            q.setOptionD(cursor.getString(cursor.getColumnIndexOrThrow("option_d")));
            q.setCorrectAnswer(cursor.getString(cursor.getColumnIndexOrThrow("correct_answer")));
            q.setOrderNum(cursor.getInt(cursor.getColumnIndexOrThrow("order_num")));
            questions.add(q);
        }
        cursor.close();
        return questions;
    }

    // --- QUIZ RESULTS ---

    public long insertQuizResult(QuizResult result) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("student_id", result.getStudentId());
        cv.put("quiz_id", result.getQuizId());
        cv.put("score", result.getScore());
        cv.put("total_questions", result.getTotalQuestions());
        cv.put("time_taken", result.getTimeTaken());
        cv.put("timer_mode", result.isTimerMode() ? 1 : 0);
        cv.put("flashcard_mode", result.isFlashcardMode() ? 1 : 0);
        cv.put("completed_at", currentTimestamp());
        return db.insert(TABLE_QUIZ_RESULTS, null, cv);
    }

    public List<QuizResult> getResultsByQuiz(int quizId) {
        List<QuizResult> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT r.*, u.username, q.title FROM " + TABLE_QUIZ_RESULTS + " r " +
                        "JOIN " + TABLE_USERS + " u ON r.student_id = u.id " +
                        "JOIN " + TABLE_QUIZZES + " q ON r.quiz_id = q.id " +
                        "WHERE r.quiz_id = ? ORDER BY r.score DESC",
                new String[]{String.valueOf(quizId)});

        while (cursor.moveToNext()) {
            QuizResult r = new QuizResult();
            r.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            r.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("student_id")));
            r.setQuizId(cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id")));
            r.setScore(cursor.getInt(cursor.getColumnIndexOrThrow("score")));
            r.setTotalQuestions(cursor.getInt(cursor.getColumnIndexOrThrow("total_questions")));
            r.setTimeTaken(cursor.getInt(cursor.getColumnIndexOrThrow("time_taken")));
            r.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            r.setQuizTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            results.add(r);
        }
        cursor.close();
        return results;
    }

    public List<QuizResult> getResultsByAdmin(int adminId) {
        List<QuizResult> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT r.*, u.username, q.title FROM " + TABLE_QUIZ_RESULTS + " r " +
                        "JOIN " + TABLE_USERS + " u ON r.student_id = u.id " +
                        "JOIN " + TABLE_QUIZZES + " q ON r.quiz_id = q.id " +
                        "WHERE q.admin_id = ? ORDER BY r.completed_at DESC",
                new String[]{String.valueOf(adminId)});

        while (cursor.moveToNext()) {
            QuizResult r = new QuizResult();
            r.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            r.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("student_id")));
            r.setQuizId(cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id")));
            r.setScore(cursor.getInt(cursor.getColumnIndexOrThrow("score")));
            r.setTotalQuestions(cursor.getInt(cursor.getColumnIndexOrThrow("total_questions")));
            r.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            r.setQuizTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            results.add(r);
        }
        cursor.close();
        return results;
    }

    // --- LEADERBOARD ---

    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT u.username, u.grade_level, u.total_points, COUNT(a.id) as badge_count " +
                        "FROM " + TABLE_USERS + " u " +
                        "LEFT JOIN " + TABLE_ACHIEVEMENTS + " a ON u.id = a.student_id " +
                        "WHERE u.role = 'student' " +
                        "GROUP BY u.id ORDER BY u.total_points DESC LIMIT 20", null);

        int rank = 1;
        while (cursor.moveToNext()) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setRank(rank++);
            entry.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            entry.setGradeLevel(cursor.getInt(cursor.getColumnIndexOrThrow("grade_level")));
            entry.setTotalPoints(cursor.getInt(cursor.getColumnIndexOrThrow("total_points")));
            entry.setBadgeCount(cursor.getInt(cursor.getColumnIndexOrThrow("badge_count")));
            entries.add(entry);
        }
        cursor.close();
        return entries;
    }

    public List<LeaderboardEntry> getLeaderboardByQuiz(int quizId) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT u.username, u.grade_level, r.score as total_points, 0 as badge_count " +
                        "FROM " + TABLE_QUIZ_RESULTS + " r " +
                        "JOIN " + TABLE_USERS + " u ON r.student_id = u.id " +
                        "WHERE r.quiz_id = ? ORDER BY r.score DESC",
                new String[]{String.valueOf(quizId)});

        int rank = 1;
        while (cursor.moveToNext()) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setRank(rank++);
            entry.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            entry.setGradeLevel(cursor.getInt(cursor.getColumnIndexOrThrow("grade_level")));
            entry.setTotalPoints(cursor.getInt(cursor.getColumnIndexOrThrow("total_points")));
            entry.setBadgeCount(0);
            entries.add(entry);
        }
        cursor.close();
        return entries;
    }

    // --- ACHIEVEMENTS ---

    public List<Achievement> getAchievementsByStudent(int studentId) {
        List<Achievement> achievements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACHIEVEMENTS, null, "student_id = ?",
                new String[]{String.valueOf(studentId)}, null, null, "earned_at DESC");

        while (cursor.moveToNext()) {
            Achievement a = new Achievement();
            a.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            a.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("student_id")));
            a.setBadgeName(cursor.getString(cursor.getColumnIndexOrThrow("badge_name")));
            a.setEarnedAt(cursor.getString(cursor.getColumnIndexOrThrow("earned_at")));
            achievements.add(a);
        }
        cursor.close();
        return achievements;
    }

    public void insertAchievement(int studentId, String badgeName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("student_id", studentId);
        cv.put("badge_name", badgeName);
        cv.put("earned_at", currentTimestamp());
        db.insert(TABLE_ACHIEVEMENTS, null, cv);
    }

    // --- CHAPTER PROGRESS ---

    public List<ChapterProgress> getChapterProgress(int studentId) {
        List<ChapterProgress> chapters = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAPTER_PROGRESS, null, "student_id = ?",
                new String[]{String.valueOf(studentId)}, null, null, "id ASC");

        while (cursor.moveToNext()) {
            ChapterProgress cp = new ChapterProgress();
            cp.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cp.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("student_id")));
            cp.setChapterName(cursor.getString(cursor.getColumnIndexOrThrow("chapter_name")));
            cp.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            cp.setProgressPercent(cursor.getInt(cursor.getColumnIndexOrThrow("progress_percent")));
            chapters.add(cp);
        }
        cursor.close();
        return chapters;
    }

    public void initChapterProgressForStudent(int studentId, boolean lowerPrimary) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_CHAPTER_PROGRESS, new String[]{"id"}, "student_id = ?",
                new String[]{String.valueOf(studentId)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return;
        }
        cursor.close();
        seedChapterProgress(db, studentId, lowerPrimary);
    }

    public void updateChapterProgress(int studentId, String chapterName, String status, int progress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        cv.put("progress_percent", progress);
        db.update(TABLE_CHAPTER_PROGRESS, cv,
                "student_id = ? AND chapter_name = ?",
                new String[]{String.valueOf(studentId), chapterName});
    }
}
