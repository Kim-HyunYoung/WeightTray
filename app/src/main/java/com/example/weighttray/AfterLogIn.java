package com.example.weighttray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AfterLogIn extends AppCompatActivity {

    TextView id;       //상단바 아이디 출력 textview

    private final int FgRecordWeight = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);
        
        //상단바 로그아웃 버튼 클릭시 메인화면으로 이동
        findViewById(R.id.topLogOutBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        //객체 가져오기
        id = findViewById(R.id.userId);

        //로그인 화면에서 넘겨준 아이디값 가져오기
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        //상단바 000님 아이디 셋팅
        id.setText(userId);

        //하단바 버튼 객체 가져오기
        ImageButton recordWeightBtn = findViewById(R.id.recordWeightBtn);

        recordWeightBtn.setOnClickListener(v -> {
            //버튼 눌렀을때 버튼 배경색상 바꾸기
            recordWeightBtn.setBackgroundColor(Color.parseColor("#fff2cc"));
            FragmentView(FgRecordWeight);
        });


        //로그인 후 첫화면의 프래그먼트 (몸무게 입력 프래그먼트) 로 설정
        FragmentView(FgRecordWeight);
        recordWeightBtn.setBackgroundColor(Color.parseColor("#fff2cc"));
    }

    //프래그먼트를 바꿔주는 함수
    private void FragmentView(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //프래그먼트로 아이디값 전달해주기 위해 Intent로
        //아이디 넘겨받기.
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        //프래그먼트로 아이디 전달해주기 (bundle 사용)
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        switch (fragment){
            case 1:
                //첫번째 프래그먼트 호출
                RecordWeight recordWeight = new RecordWeight();
                recordWeight.setArguments(bundle);      //아이디값 넘겨주기
                transaction.replace(R.id.fragment_container, recordWeight);
                transaction.commit();
                break;

        }
    }
}