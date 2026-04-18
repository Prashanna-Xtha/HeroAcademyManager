package com.herocademy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.herocademy.adapter.HeroAdapter;
import com.herocademy.model.Hero;
import com.herocademy.storage.HeroStorage;

import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    private HeroAdapter adapter;
    private HeroStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        storage = HeroStorage.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTraining);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HeroAdapter(storage.getHeroesByLocation("TRAINING"), true);
        recyclerView.setAdapter(adapter);

        Button btnTrain      = findViewById(R.id.btnTrain);
        Button btnSendToDorms = findViewById(R.id.btnSendToDorms);

        btnTrain.setOnClickListener(v       -> trainHeroes());
        btnSendToDorms.setOnClickListener(v -> sendToDorms());
    }

    private void trainHero(Hero hero) {
        String result = hero.train();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    private void moveToQuarters(Hero hero) {
        hero.resetEnergy();
        hero.setLocation("DORMS");
    }

    private void trainHeroes() {
        List<Hero> selected = adapter.getSelectedHeroes();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select at least one hero to train!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Hero hero : selected) {
            trainHero(hero);
        }
        storage.saveData(this);
        adapter.updateList(storage.getHeroesByLocation("TRAINING"));
    }

    private void sendToDorms() {
        List<Hero> selected = adapter.getSelectedHeroes();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select at least one hero to send back!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Hero hero : selected) {
            moveToQuarters(hero);
        }
        storage.saveData(this);
        adapter.updateList(storage.getHeroesByLocation("TRAINING"));
        Toast.makeText(this, selected.size() + " hero(es) sent to Dorms to recover!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateList(storage.getHeroesByLocation("TRAINING"));
    }
}