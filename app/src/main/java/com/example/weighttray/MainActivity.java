package com.example.weighttray;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //상단반의 회원가입 버튼을 눌렀을 때 회원가입 페이지로 이동
        findViewById(R.id.topSignupBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), signUp.class);
            startActivity(intent);
        });

        //상단바의 로그인 버튼을 눌렀을 때 로그인 페이지로 이동
        findViewById(R.id.topLogInBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), logIn.class);
            startActivity(intent);
        });

        //지금 시작하기 버튼을 눌렀을 때 로그인 페이지로 이동
        findViewById(R.id.start).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), logIn.class);
            startActivity(intent);
        });


    }
}