package com.herocademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.herocademy.storage.HeroStorage;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounts;
    private HeroStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = HeroStorage.getInstance();
        storage.loadData(this); // load saved heroes when app opens

        tvCounts = findViewById(R.id.tvCounts);

        Button btnRecruit  = findViewById(R.id.btnRecruit);
        Button btnDorms    = findViewById(R.id.btnDorms);
        Button btnTraining = findViewById(R.id.btnTraining);
        Button btnQuest    = findViewById(R.id.btnQuest);

        btnRecruit.setOnClickListener(v  -> navigateTo(RecruitActivity.class));
        btnDorms.setOnClickListener(v    -> navigateTo(DormsActivity.class));
        btnTraining.setOnClickListener(v -> navigateTo(TrainingActivity.class));
        btnQuest.setOnClickListener(v    -> navigateTo(QuestActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounts();
    }

    private void updateCounts() {
        int dorms    = storage.getHeroesByLocation("DORMS").size();
        int training = storage.getHeroesByLocation("TRAINING").size();
        int quest    = storage.getHeroesByLocation("QUEST").size();
        tvCounts.setText("Dorms: " + dorms + "  |  Training: " + training + "  |  Quest Board: " + quest);
    }

    private void navigateTo(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }
}