package com.starla.mathletics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starla.mathletics.database.DatabaseHelper;
import com.starla.mathletics.models.User;
import com.starla.mathletics.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MathLetics:Login";

    private EditText etUsername, etPassword;
    private RadioGroup rgRole;
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate()");

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            navigateToHome();
            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        Button btnGo = findViewById(R.id.btnGo);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnGo.setOnClickListener(v -> attemptLogin());
        btnSignUp.setOnClickListener(v -> openSignup());
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        RadioButton rbStudent = findViewById(R.id.rbStudent);
        String role = rbStudent.isChecked() ? "student" : "admin";

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.authenticate(username, password, role);
        if (user != null) {
            session.createSession(user.getId(), user.getUsername(), user.getRole(), user.getGradeLevel());
            Toast.makeText(this, "Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
            navigateToHome();
        } else {
            Toast.makeText(this, "Authentication failed. Check credentials and role.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSignup() {
        RadioButton rbStudent = findViewById(R.id.rbStudent);
        Intent intent;
        if (rbStudent.isChecked()) {
            intent = new Intent(this, StudentSignupActivity.class);
        } else {
            intent = new Intent(this, AdminSignupActivity.class);
        }
        startActivity(intent);
    }

    private void navigateToHome() {
        Intent intent;
        if (session.isStudent()) {
            intent = new Intent(this, StudentHomeActivity.class);
        } else {
            intent = new Intent(this, AdminHomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
