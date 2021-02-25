package com.example.weighttray;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signUp extends AppCompatActivity {

    EditText idEditText;    //사용자가 입력한 아이디
    EditText pwEditText;      //사용자가 입력한 비밀번호
    EditText emailEditText;   //이메일
    EditText goalWeightEditText;  //목표 몸무게

    TextView idCheckMsg, msg;   //아이디 중복 체크 후 메세지 출력, 빈값이 있는지 확인메세지 출력

    Button signUpBtn;  //가입하기 버튼

   //데이터베이스 헬퍼
    DatabaseHelper dbHelper;

    //데이터베이스 변수 생성
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //객체 가져오기
        idEditText = (EditText)findViewById(R.id.idEdText);
        pwEditText = (EditText)findViewById(R.id.pwEdText);
        emailEditText = (EditText)findViewById(R.id.emailEdText);
        goalWeightEditText = (EditText)findViewById(R.id.goalWeightEdText);

        idCheckMsg = findViewById(R.id.idCheckTv);  //아이디 중복 체크 출력할 텍스트뷰
        msg = findViewById(R.id.msg);   //모든 사항 입력했는지 확인해서 메세지 출력할 텍스트뷰

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

        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setEnabled(false);    //비활성화
        signUpBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));  //버튼 색깔 회색으로 설정(비활성화 표현)

        //헬퍼를 이용하여 생성
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();  //쓸 수 있는 데이터베이스를 가져옴.

        Log.i("MyTag", "디비를 생성함.");

        Button idCheck = findViewById(R.id.idCheckBtn);
        //아이디 중복 확인
        idCheck.setOnClickListener(v -> {
            String id = idEditText.getText().toString();
            String sql ="select count(*) from user where user_id='"+id+"'";

            Cursor cursor = database.rawQuery(sql,null);    //select 할때 cursor를 사용.
            //값 가져오기
            int result = 0;     //조회 결과 값
            while(cursor.moveToNext()){
                result = cursor.getInt(0);
            }

            if(TextUtils.isEmpty(id)){      //입력이 안되었다면
                idCheckMsg.setText("아이디를 입력해주세요.");
                signUpBtn.setEnabled(false);    //버튼 비활성화
            }else if(result == 0){
                idCheckMsg.setText("사용가능한 아이디입니다.");
                signUpBtn.setEnabled(true);     //버튼 활성화
                signUpBtn.setBackgroundColor(Color.parseColor("#fff2cc"));
            }else{
                idCheckMsg.setText("이미 등록된 아이디입니다.");
                signUpBtn.setEnabled(false);    //버튼 비활성화
            }

            cursor.close();     //cursor 닫아줘야함.
        });

        //마지막 입력 사항인 목표 몸무게에서 포커스가 벗어나면 
        //메세지 출력, 버튼 비활성화
        goalWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //사용자가 입력한 값 가져오기
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String goalWeightStr = goalWeightEditText.getText().toString();
                if(!hasFocus){  //포커스를 잃었을 때
                    if(id.equals("") || pw.equals("") || email.equals("") || goalWeightStr.equals("")){
                        msg.setText("필수입력사항을 모두 입력해주세요.");
                        signUpBtn.setEnabled(false);
                        signUpBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    }
                }else{
                    msg.setText("");
                    signUpBtn.setEnabled(true);
                    signUpBtn.setBackgroundColor(Color.parseColor("#fff2cc"));
                }
            }
        });

        signUpBtn.setOnClickListener(v -> {
            //사용자가 입력한 값 가져오기
            String id = idEditText.getText().toString();
            String pw = pwEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String goalWeightStr = goalWeightEditText.getText().toString();
            
            float goalWeight = Float.parseFloat(goalWeightStr);

            //테이블에 입력받은 값 넣어주기
            String sql = "insert into user(user_id,password,email,weight_goal) "+
                    " values('"+id + "', '" + pw + "', '" +email+ "', " +goalWeight +")";
            database.execSQL(sql);
            Log.i("MyTag", "데이터 추가");

            //로그인 화면으로 전환
            Intent intent = new Intent(getApplicationContext(), logIn.class);
            startActivity(intent);
            
        });
//        dbHelper.close();
//        database.close();
    }
}