package com.example.tasker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tasker.MainActivity;
import com.example.tasker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;
    private ProgressBar loginCircleBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        CheckBox ckbShowPassword = findViewById(R.id.ckb_show_password);
        loginCircleBar = findViewById(R.id.progress_circle_login);

        //Login Method
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmailStr = emailLogin.getText().toString();
                String loginPasswordStr = passwordLogin.getText().toString();

                if (!TextUtils.isEmpty(loginEmailStr) || !TextUtils.isEmpty(loginPasswordStr)) {
                    loginCircleBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmailStr, loginPasswordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                openPrincipalScreen();
                            } else {
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();
                                loginCircleBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        //Show password with checkbox
        ckbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Navigate to Register Page
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openPrincipalScreen() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}