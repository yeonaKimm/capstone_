package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

/**
 * RecruitEvaluation 클래스는 평가 화면을 관리합니다.
 */
public class RecruitEvaluation extends AppCompatActivity {

    private ImageView[] stars;
    private int rating = 0;
    private EditText reviewEditText;
    private RecruitEvaluationDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_evaluation);

        // 별점 이미지뷰 배열 초기화
        stars = new ImageView[5];
        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);
        reviewEditText = findViewById(R.id.review);

        // DB 헬퍼 초기화
        dbHelper = new RecruitEvaluationDBHelper(this);
    }

    /**
     * 별점 클릭 시 호출되는 메서드
     *
     * @param view 클릭된 별점 이미지뷰
     */
    public void onStarClick(View view) {
        // 클릭된 별점의 태그를 정수로 변환하여 평가 점수를 설정합니다.
        int selectedStar = Integer.parseInt(view.getTag().toString());
        rating = selectedStar;

        // 선택된 별점까지 별 아이콘을 설정합니다.
        for (int i = 0; i < stars.length; i++) {
            if (i < selectedStar) {
                stars[i].setImageResource(R.drawable.ic_star);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_none);
            }
        }
    }

    /**
     * 평가 등록 버튼 클릭 시 호출되는 메서드
     *
     * @param view 클릭된 버튼
     */
    public void onEvaluationClick(View view) {
        String review = reviewEditText.getText().toString();

        // 별점과 리뷰 텍스트가 입력되었는지 확인합니다.
        if (rating == 0 || review.isEmpty()) {
            Toast.makeText(this, "별점과 한줄평을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // DB에 평가 데이터를 삽입하고 결과를 확인합니다.
        boolean isInserted = dbHelper.insertEvaluation(rating, review);
        if (isInserted) {
            Toast.makeText(this, "평가가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "평가 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
