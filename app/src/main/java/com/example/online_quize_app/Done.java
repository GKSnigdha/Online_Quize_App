package com.example.online_quize_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.online_quize_app.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Common.Common;

public class Done extends AppCompatActivity {
    Button btnTryAgain;
    TextView txtResultScore,getTextResultQuestion;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        database=FirebaseDatabase.getInstance();
        question_score=database.getReference("Question_score");

        txtResultScore=findViewById(R.id.textTotalScore);
        getTextResultQuestion=findViewById(R.id.txtTotalQuestion);
        progressBar=findViewById(R.id.doneProgrssBar);
        btnTryAgain=findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        // Get data from bundle and set to view
        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            int score=extra.getInt("SCORE");
            int totalQuestion=extra.getInt("TOTAL");
            int correctAnswer=extra.getInt("CORRECT");

            Log.e("Score", String.valueOf(score));
            Log.e("total", String.valueOf(totalQuestion));
            Log.e("correct", String.valueOf(correctAnswer));

            txtResultScore.setText(String.format("SCORE : %d",score));
            getTextResultQuestion.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion));
            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //Upload point to DB
            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),Common.categoryId)).setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),Common.categoryId),Common.currentUser.getUserName(),String.valueOf(score)));

        }
    }
}
