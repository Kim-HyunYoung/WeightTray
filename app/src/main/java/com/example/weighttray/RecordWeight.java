package com.example.weighttray;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RecordWeight extends Fragment {

    //데이터베이스 헬퍼
    DatabaseHelper dbHelper;

    SQLiteDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_weight, container, false);

        //헬퍼를 이용하여 생성
        //프래그먼트는 context를 상속받지 않기 때문에 this, getApplicationContext()가 안됨.
        //그래서 getActivity()를 해줘야함.
        AfterLogIn afterLogIn = (AfterLogIn)getActivity();
        dbHelper = new DatabaseHelper(afterLogIn);
        database = dbHelper.getWritableDatabase();  //쓸 수 있는 데이터베이스를 가져옴.

        //calendar 객체 가져오기 -> 오늘날짜 가져오기 (요일까지)
        Date currentTime = Calendar.getInstance().getTime();
        String dateStr = new SimpleDateFormat("yyyy.MM.dd EE", Locale.getDefault()).format(currentTime);

        //오늘 날짜 보여줄 텍스트 뷰 객체가져오기, 요일까지 나오게 set
        TextView today = (TextView)view.findViewById(R.id.today);
        today.setText(dateStr);

        //AfterLogIn 액티비티에서 넘긴 아이디 값 받기
        String userId = getArguments().getString("userId");

        //어제날짜 구하기
        Calendar calendar = Calendar.getInstance(); //calendar 객체 가져오기
        calendar.add(Calendar.DATE, -1);
        String yesterDayStr = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(calendar.getTime());

        //"어제는 ~~~" 보여줄 텍스트뷰 객체 가져오기.
        TextView yesterday = (TextView)view.findViewById(R.id.yesterday);
        TextView weightTv = (TextView)view.findViewById(R.id.weight);
        TextView end = (TextView)view.findViewById(R.id.end);

        //어제 날짜에 입력되었는지 확인
        String yesterday_sql = "select count(*) from weight_info where date='" + yesterDayStr
                +"' and user_id='" +userId +"'";
        Cursor cursor1 = database.rawQuery(yesterday_sql, null);
        int yesterResult = 0;
        while(cursor1.moveToNext()){
            yesterResult = cursor1.getInt(0);
        }

        if(yesterResult==0){    //어제 날짜에 몸무게가 입력되지 않았다면
            yesterday.setText("어제 몸무게가 입력되지 않았습니다.");
            weightTv.setText("");
            end.setText("");
        }else{  //입력된 몸무게가 있다면
            //입력된 몸무게 조회
            String selectWeight_sql = "select weight from weight_info where date='"+yesterDayStr+
                    "' and user_id='"+userId+"'";
            Cursor cursor2 = database.rawQuery(selectWeight_sql, null);
            float weight = 0;
            while(cursor2.moveToNext()) {
                weight = cursor2.getFloat(0);
            }
            yesterday.setText("어제는 ");
            weightTv.setText(weight +" ");
            end.setText("kg 이였습니다.");
        }

        //디비에 저장할 오늘날짜
        String today_db = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(currentTime);

        //등록 버튼
        Button insertBtn = (Button)view.findViewById(R.id.insertBtn);
        insertBtn.setOnClickListener(v -> {
            //사용자가 입력한 몸무게
            EditText weightEditText = (EditText)view.findViewById(R.id.weightEdText);
            String weightStr = weightEditText.getText().toString();
            float weight_db = 0;

            //몸무게 미입력시 텍스트 출력
            TextView msg = (TextView)view.findViewById(R.id.weightMsg);
            if(weightStr.equals("")){
                msg.setText("몸무게를 입력해주세요!!!");
            }else{
                msg.setText("");
                weight_db = Float.parseFloat(weightStr);

                //날짜가 같다면 update 쿼리문 실행, 조건(아이디, 날짜)
                String selectSql = "select count(date) from weight_info where user_id='" +userId+
                        "' and date='" + today_db +"'";
                Cursor cursor = database.rawQuery(selectSql,null);
                int result = 0;     //조회 결과값
                while (cursor.moveToNext()){
                    result = cursor.getInt(0);
                }

                if(result == 0){    //몸무게가 입력되지 않았다면
                    //몸무게 테이블에 데이터 넣기
                    String sql = "insert into weight_info(user_id,weight,date) "+
                            " values('" + userId + "', " + weight_db + ", '" +today_db+ "')";
                    database.execSQL(sql);
                }else{
                    //업데이트 해주기 (몸무게 수정)
                    String sql = "update weight_info set weight="+weight_db+
                            " where user_id='" +userId+ "' and date='"+today_db+"'";
                    database.execSQL(sql);
                }

                //입력 완료하면 토스트 메세지 화면 가운데에 띄우기
                Toast toast = Toast.makeText(getActivity(), "입력 완료!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,100);
                toast.show();
                cursor.close();
            }

        });

        return view;
    }

}