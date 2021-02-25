package com.example.weighttray;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class logIn extends AppCompatActivity {

    EditText idEt;
    EditText pwEt;
    
    TextView msg;

    //데이터베이스 헬퍼
    DatabaseHelper dbHelper;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //상단바 회원가입 버튼
        findViewById(R.id.topSignupBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), signUp.class);
            startActivity(intent);
        });

        //상단바 로그인 버튼
        findViewById(R.id.topLogInBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), logIn.class);
            startActivity(intent);
        });

        //객체 가져오기
        idEt = findViewById(R.id.idEditText);
        pwEt = findViewById(R.id.pwEditText);
        
        msg = findViewById(R.id.message);

        //헬퍼를 이용하여 생성
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();  //쓸 수 있는 데이터베이스를 가져옴.

        Log.i("MyTag", "데이터베이스 생성");

        Button loginBtn = findViewById(R.id.loginBtn);  //로그인 버튼

        //비밀번호 입력창에서 포커스가 벗어났을 때
        //아이디, 비밀번호를 다 입력했는지 확인하기 위해
        pwEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    //값 가져오기
                    String id = idEt.getText().toString();
                    String pw = pwEt.getText().toString();

                    if(id.equals("") || pw.equals("")){
                        msg.setText("아이디 또는 비밀번호가 입력되지 않았습니다.");
                        loginBtn.setEnabled(false);     //로그인 버튼 비활성화
                        loginBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    }else{
                        msg.setText("");
                        loginBtn.setEnabled(true);
                        loginBtn.setBackgroundColor(Color.parseColor("#fff2cc"));
                    }
                }
            }
        });

        loginBtn.setOnClickListener(v -> {
            //값 가져오기
            String id = idEt.getText().toString();
            String pw = pwEt.getText().toString();

            String sql = "select count(*) from user where user_id='"+id+"' and password='"+pw+"'";

            Cursor cursor = database.rawQuery(sql, null);
            //쿼리문 결과 가져오기
            int result = 0;
            while(cursor.moveToNext()){
                result = cursor.getInt(0);
            }

            if(result == 1){    //로그인 성공
                //몸무게 입력 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), AfterLogIn.class);
                intent.putExtra("userId", id);  //아이디 전달하기
                startActivity(intent);
            }else{  //로그인 실패
                msg.setText("아이디 또는 비밀번호를 다시 입력해주세요.");
            }

            cursor.close();     //cursor 닫아줘야함.
        });


    }
}