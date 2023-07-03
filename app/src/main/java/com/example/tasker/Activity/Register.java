package com.example.tasker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tasker.MainActivity;
import com.example.tasker.Model.UserModel;
import com.example.tasker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText emailRegister;
    private EditText usernameRegister;
    private EditText passwordRegister;
    private EditText confirmPasswordRegister;
    private ProgressBar progressBarRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailRegister = findViewById(R.id.email_register);
        usernameRegister = findViewById(R.id.username_register);
        passwordRegister = findViewById(R.id.password_register);
        confirmPasswordRegister = findViewById(R.id.password_second_register);
        CheckBox ckbShowPasswordRegister = findViewById(R.id.ckb_show_password_register);
        Button btnRegisterRegister = findViewById(R.id.btn_register_register);
        Button btnLoginRegister = findViewById(R.id.btn_login_register);
        progressBarRegister = findViewById(R.id.progress_circle_register);

        //Show password with checkbox
        ckbShowPasswordRegister.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                passwordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                passwordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        //Register Method
        btnRegisterRegister.setOnClickListener(view -> {

            UserModel userModel = new UserModel();

            userModel.setEmail(emailRegister.getText().toString());
            userModel.setUsername(usernameRegister.getText().toString());
            String registerPassword = passwordRegister.getText().toString();
            String registerConfirmPassword = confirmPasswordRegister.getText().toString();

            //Validate if inputs are not empty
            if (!TextUtils.isEmpty(userModel.getEmail()) && !TextUtils.isEmpty(userModel.getUsername()) && !TextUtils.isEmpty(registerPassword) && !TextUtils.isEmpty(registerConfirmPassword)){
                //Validate both passwords
                if (registerPassword.equals(registerConfirmPassword)){
                    progressBarRegister.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(userModel.getEmail(), registerPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            userModel.setId(mAuth.getUid());
                            userModel.saveUser();
                            System.out.println("ok");
                            openActivityScreen();
                        }else {
                            String error;
                            try{
                                throw Objects.requireNonNull(task.getException());
                            }catch (FirebaseAuthWeakPasswordException e){
                                error = "Password must have 6 characters";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                error = "Invalid Email";
                            }catch (FirebaseAuthUserCollisionException e){
                                error = "Username already exists";
                            }catch (Exception e){
                                error = "Error in register";
                                e.printStackTrace();
                            }
                            Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
                        }
                        progressBarRegister.setVisibility(View.INVISIBLE);
                    });
                }else {
                    Toast.makeText(Register.this,"Passwords are different", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Register.this,"Fill all inputs", Toast.LENGTH_SHORT).show();
            }
        });

        //Go to login Page
        btnLoginRegister.setOnClickListener(view -> openLoginScreen());
    }

    private void openActivityScreen() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openLoginScreen() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}