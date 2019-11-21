package com.example.carbnzero;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText mTextMPG;
    Button mButtonRegister;
    TextView mTextViewLogin ;
    public static float mpg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mTextPassword.setTransformationMethod(new MainActivity.AsteriskPasswordTransformationMethod());
        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
        mTextCnfPassword.setTransformationMethod(new MainActivity.AsteriskPasswordTransformationMethod());
        mButtonRegister = findViewById(R.id.button_register);
        mTextViewLogin = findViewById(R.id.textview_login);
        mTextMPG = findViewById(R.id.edittext_mpg);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(LoginIntent);
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                mpg = Float.valueOf(mTextMPG.getText().toString());
                Boolean reg = db.checkUser(user, pwd);
                if(pwd.equals(cnf_pwd) && mpg > 0 && mpg <=100)
                {
                    if(reg)
                    {
                        Toast.makeText(RegisterActivity.this, "User already exists",Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        long val = db.addUser(user, pwd, mpg);
                        if (val > 0)
                        {
                            Toast.makeText(RegisterActivity.this, "Registration complete", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(moveToLogin);
                        }
                        else
                            {
                            Toast.makeText(RegisterActivity.this, "Registration has failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    if(mpg <= 0 || mpg > 100)
                    {
                        Toast.makeText(RegisterActivity.this, "Enter a valid mpg", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}