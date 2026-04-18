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

public class DormsActivity extends AppCompatActivity {

    private HeroAdapter adapter;
    private HeroStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dorms);

        storage = HeroStorage.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HeroAdapter(storage.getHeroesByLocation("DORMS"), true);
        recyclerView.setAdapter(adapter);

        Button btnMoveToTraining = findViewById(R.id.btnMoveToTraining);
        Button btnMoveToQuest    = findViewById(R.id.btnMoveToQuest);

        btnMoveToTraining.setOnClickListener(v -> moveHeroes("TRAINING"));
        btnMoveToQuest.setOnClickListener(v    -> moveHeroes("QUEST"));
    }

    private void moveHero(int id, String location) {
        Hero hero = storage.getHero(id);
        if (hero != null) {
            hero.setLocation(location);
        }
    }

    private void moveHeroes(String destination) {
        List<Hero> selected = adapter.getSelectedHeroes();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select at least one hero first!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Hero hero : selected) {
            moveHero(hero.getId(), destination);
        }
        storage.saveData(this);
        adapter.updateList(storage.getHeroesByLocation("DORMS"));
        Toast.makeText(this, selected.size() + " hero(es) moved to " + destination + "!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateList(storage.getHeroesByLocation("DORMS"));
    }
}