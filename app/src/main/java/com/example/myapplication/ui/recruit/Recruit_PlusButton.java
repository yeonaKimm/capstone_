package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.Calendar;

public class Recruit_PlusButton extends AppCompatActivity {

    // UI 요소 선언
    private EditText dateEditText, timeEditText, placeEditText, accountEditText, priceEditText, divisionEditText, paymentEditText, promiseEditText;
    private Spinner bankSpinner;
    private RecruitPlusDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_plusbutton);

        // 레이아웃으로부터 뷰 초기화
        dateEditText = findViewById(R.id.date); // 날짜 입력을 위한 EditText
        timeEditText = findViewById(R.id.time); // 시간 입력을 위한 EditText
        placeEditText = findViewById(R.id.place); // 장소 입력을 위한 EditText
        accountEditText = findViewById(R.id.account); // 계좌 입력을 위한 EditText
        priceEditText = findViewById(R.id.price); // 가격 입력을 위한 EditText
        divisionEditText = findViewById(R.id.division); // 분할 입력을 위한 EditText
        paymentEditText = findViewById(R.id.payment); // 계산된 결제액 표시를 위한 EditText
        promiseEditText = findViewById(R.id.promise); // 약속사항 입력을 위한 EditText
        bankSpinner = findViewById(R.id.spinner_bank); // 은행 선택을 위한 Spinner

        // 데이터베이스 도우미 초기화
        dbHelper = new RecruitPlusDBHelper(this);

        // Spinner 설정
        String[] bankOptions = {"은행명", "국민은행", "농협은행", "신한은행", "카카오뱅크"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bankOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(adapter);

        // 날짜 선택 EditText 클릭 시 날짜 선택 다이얼로그 표시
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 시간 선택 EditText 클릭 시 시간 선택 다이얼로그 표시
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // priceEditText와 divisionEditText에 대한 TextWatcher 설정
        priceEditText.addTextChangedListener(paymentCalculator); // 가격 EditText에 대한 텍스트 변경 리스너 추가
        divisionEditText.addTextChangedListener(paymentCalculator); // 분할 EditText에 대한 텍스트 변경 리스너 추가

        // 저장 버튼 클릭 시 데이터베이스에 저장
        findViewById(R.id.sendBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    // 날짜 선택 다이얼로그를 표시하는 메소드
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // 선택된 날짜를 EditText에 설정합니다
                        dateEditText.setText(String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth));
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // 시간 선택 다이얼로그를 표시하는 메소드
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        // 선택된 시간을 EditText에 설정합니다
                        timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    // TextWatcher 인터페이스를 구현하여 자동 계산 기능 구현
    private TextWatcher paymentCalculator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // 이 구현에서는 사용하지 않음
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 이 구현에서는 사용하지 않음
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculatePayment(); // 가격과 분할에 기반한 결제액 계산 메서드 호출
        }
    };

    // 결제액 계산 로직 구현
    private void calculatePayment() {
        String priceStr = priceEditText.getText().toString().trim(); // 가격 입력값을 문자열로 가져옴
        String divisionStr = divisionEditText.getText().toString().trim(); // 분할 입력값을 문자열로 가져옴

        if (!priceStr.isEmpty() && !divisionStr.isEmpty()) {
            try {
                int price = Integer.parseInt(priceStr); // 가격을 정수로 변환
                int division = Integer.parseInt(divisionStr); // 분할을 정수로 변환
                if (division != 0) {
                    int calculatedPayment = price / division; // 계산된 결제액 계산
                    paymentEditText.setText(String.valueOf(calculatedPayment)); // 계산된 결제액을 EditText에 설정
                } else {
                    paymentEditText.setText(""); // 분할이 0이면 EditText를 비움
                    Toast.makeText(this, "분할은 0이 될 수 없습니다", Toast.LENGTH_SHORT).show(); // 사용자에게 메시지 표시
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                paymentEditText.setText(""); // 예외 발생 시 EditText를 비움
                Toast.makeText(this, "유효하지 않은 입력입니다", Toast.LENGTH_SHORT).show(); // 사용자에게 메시지 표시
            }
        } else {
            paymentEditText.setText(""); // 입력값이 비어 있으면 EditText를 비움
        }
    }

    private void saveData() {
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String bank = bankSpinner.getSelectedItem().toString();
        String account = accountEditText.getText().toString();
        Integer price = Integer.valueOf(priceEditText.getText().toString()); // Integer.valueOf로 변환
        Integer division = Integer.valueOf(divisionEditText.getText().toString()); // Integer.valueOf로 변환
        Integer payment = Integer.valueOf(paymentEditText.getText().toString());
        String promise = promiseEditText.getText().toString();

        boolean isInserted = dbHelper.insertData(date, time, place, bank, account, price, division, payment, promise); // promise 추가
        if (isInserted) {
            // 데이터 삽입 성공
        } else {
            // 데이터 삽입 실패
        }
    }

    // 데이터 삽입 버튼 클릭 시 호출되는 메서드
    public void onInsertClick(View view) {
        String date = dateEditText.getText().toString().trim(); // 날짜 입력값을 문자열로 가져옴
        String time = timeEditText.getText().toString().trim(); // 시간 입력값을 문자열로 가져옴
        String place = placeEditText.getText().toString().trim(); // 장소 입력값을 문자열로 가져옴
        String bank = bankSpinner.getSelectedItem().toString(); // 선택된 은행을 문자열로 가져옴
        String account = accountEditText.getText().toString().trim(); // 계좌 입력값을 문자열로 가져옴
        String priceStr = priceEditText.getText().toString().trim(); // 가격 입력값을 문자열로 가져옴
        String divisionStr = divisionEditText.getText().toString().trim(); // 분할 입력값을 문자열로 가져옴
        String paymentStr = paymentEditText.getText().toString().trim(); // 결제액 입력값을 문자열로 가져옴
        String promise = promiseEditText.getText().toString().trim(); // 약속사항 입력값을 문자열로 가져옴

        // 모든 필드가 비어 있지 않은지 확인
        if (!date.isEmpty() && !time.isEmpty() && !place.isEmpty() && !bank.isEmpty() && !account.isEmpty() &&
                !priceStr.isEmpty() && !divisionStr.isEmpty() && !paymentStr.isEmpty() && !promise.isEmpty()) {
            try {
                int price = Integer.parseInt(priceStr); // 가격을 정수로 변환
                int division = Integer.parseInt(divisionStr); // 분할을 정수로 변환
                int payment = Integer.parseInt(paymentStr); // 결제액을 정수로 변환

                // 데이터베이스에 데이터 삽입 시도
                boolean isInserted = dbHelper.insertData(date, time, place, bank, account, price, division, payment, promise);
                if (isInserted) {
                    Toast.makeText(this, "데이터 삽입 성공", Toast.LENGTH_SHORT).show(); // 성공 메시지 표시
                } else {
                    Toast.makeText(this, "데이터 삽입 실패", Toast.LENGTH_SHORT).show(); // 실패 메시지 표시
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "숫자 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show(); // 잘못된 입력 메시지 표시
            }
        } else {
            Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show(); // 필수 입력 필드 메시지 표시
        }
    }

    // 데이터 조회 버튼 클릭 시 호출되는 메서드
    public void onViewClick(View view) {
        Cursor res = dbHelper.getAllData(); // 모든 데이터를 조회하는 Cursor 객체 가져오기
        if (res.getCount() == 0) { // 조회 결과가 없는 경우
            showMessage("오류", "데이터를 찾을 수 없습니다"); // 오류 메시지 표시
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            // 조회된 데이터를 문자열로 변환하여 StringBuilder에 추가
            buffer.append("ID: ").append(res.getString(0)).append("\n");
            buffer.append("Date: ").append(res.getString(1)).append("\n");
            buffer.append("Time: ").append(res.getString(2)).append("\n");
            buffer.append("Place: ").append(res.getString(3)).append("\n");
            buffer.append("Bank: ").append(res.getString(4)).append("\n");
            buffer.append("Account: ").append(res.getString(5)).append("\n");
            buffer.append("Price: ").append(res.getInt(6)).append("\n");
            buffer.append("Division: ").append(res.getInt(7)).append("\n");
            buffer.append("Payment: ").append(res.getInt(8)).append("\n");
            buffer.append("Promise: ").append(res.getString(9)).append("\n\n");
        }

        showMessage("데이터", buffer.toString()); // 조회된 데이터를 다이얼로그로 표시
    }

    // 데이터 삭제 버튼 클릭 시 호출되는 메서드
    public void onDeleteClick(View view) {
        String id = "1"; // 삭제할 예제 ID, 실제 로직에 따라 변경 필요
        Integer deletedRows = dbHelper.deleteData(id); // ID에 해당하는 데이터 삭제 시도
        if (deletedRows > 0) {
            Toast.makeText(this, "데이터 삭제됨", Toast.LENGTH_SHORT).show(); // 삭제 성공 메시지 표시
        } else {
            Toast.makeText(this, "데이터 삭제 실패", Toast.LENGTH_SHORT).show(); // 삭제 실패 메시지 표시
        }
    }

    // 다이얼로그 형태로 메시지 표시하는 메서드
    private void showMessage(String title, String message) {
        Toast.makeText(this, title + ": \n" + message, Toast.LENGTH_LONG).show();
    }

    // 데이터베이스 도우미 클래스 정의
    public class RecruitPlusDBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "recruitPlus.db"; // 데이터베이스 이름
        private static final int DATABASE_VERSION = 1; // 데이터베이스 버전

        private static final String TABLE_NAME = "RecruitPlus"; // 테이블 이름

        // 테이블의 열 이름 상수 선언
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_DATE = "date";
        private static final String COLUMN_TIME = "time";
        private static final String COLUMN_PLACE = "place";
        private static final String COLUMN_BANK = "bank";
        private static final String COLUMN_ACCOUNT = "account";
        private static final String COLUMN_PRICE = "price";
        private static final String COLUMN_DIVISION = "division";
        private static final String COLUMN_PAYMENT = "payment";
        private static final String COLUMN_PROMISE = "promise";

        // 테이블 생성 SQL 쿼리
        private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_TIME + " TEXT, " +
                        COLUMN_PLACE + " TEXT, " +
                        COLUMN_BANK + " TEXT, " +
                        COLUMN_ACCOUNT + " TEXT, " +
                        COLUMN_PRICE + " INTEGER, " +
                        COLUMN_DIVISION + " INTEGER, " +
                        COLUMN_PAYMENT + " INTEGER, " +
                        COLUMN_PROMISE + " TEXT);";

        public RecruitPlusDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE); // 데이터베이스 생성 시 테이블 생성 SQL 실행
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // 데이터베이스 업그레이드 시 기존 테이블 삭제
            onCreate(db); // 새로운 테이블 생성
        }

        // 데이터 삽입 메서드
        public boolean insertData(String date, String time, String place, String bank, String account, int price, int division, int payment, String promise) {
            SQLiteDatabase db = this.getWritableDatabase(); // 쓰기 가능한 데이터베이스 인스턴스 가져오기
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date); // 값 설정
            contentValues.put(COLUMN_TIME, time);
            contentValues.put(COLUMN_PLACE, place);
            contentValues.put(COLUMN_BANK, bank);
            contentValues.put(COLUMN_ACCOUNT, account);
            contentValues.put(COLUMN_PRICE, price);
            contentValues.put(COLUMN_DIVISION, division);
            contentValues.put(COLUMN_PAYMENT, payment);
            contentValues.put(COLUMN_PROMISE, promise);
            long result = db.insert(TABLE_NAME, null, contentValues); // 데이터 삽입 시도
            return result != -1; // 결과 반환
        }

        // 모든 데이터 조회 메서드
        public Cursor getAllData() {
            SQLiteDatabase db = this.getWritableDatabase(); // 쓰기 가능한 데이터베이스 인스턴스 가져오기
            return db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // 모든 데이터 조회
        }

        // 데이터 업데이트 메서드
        public boolean updateData(String id, String date, String time, String place, String bank, String account, int price, int division, int payment, String promise) {
            SQLiteDatabase db = this.getWritableDatabase(); // 쓰기 가능한 데이터베이스 인스턴스 가져오기
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date); // 값 설정
            contentValues.put(COLUMN_TIME, time);
            contentValues.put(COLUMN_PLACE, place);
            contentValues.put(COLUMN_BANK, bank);
            contentValues.put(COLUMN_ACCOUNT, account);
            contentValues.put(COLUMN_PRICE, price);
            contentValues.put(COLUMN_DIVISION, division);
            contentValues.put(COLUMN_PAYMENT, payment);
            contentValues.put(COLUMN_PROMISE, promise);
            int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id}); // 데이터 업데이트 시도
            return result > 0; // 결과 반환
        }

        // 데이터 삭제 메서드
        public Integer deleteData(String id) {
            SQLiteDatabase db = this.getWritableDatabase(); // 쓰기 가능한 데이터베이스 인스턴스 가져오기
            return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id}); // 데이터 삭제 시도
        }
    }
}
