package com.herocademy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herocademy.model.Archer;
import com.herocademy.model.Healer;
import com.herocademy.model.Hero;
import com.herocademy.model.Mage;
import com.herocademy.model.Rogue;
import com.herocademy.model.Warrior;
import com.herocademy.storage.HeroStorage;

public class RecruitActivity extends AppCompatActivity {

    private EditText etHeroName;
    private RadioGroup radioGroupClass;
    private HeroStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        storage = HeroStorage.getInstance();

        etHeroName     = findViewById(R.id.etHeroName);
        radioGroupClass = findViewById(R.id.radioGroupClass);

        Button btnCreate = findViewById(R.id.btnCreateHero);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(v -> recruitHero());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void recruitHero(String name, String heroClass) {
        Hero hero;
        switch (heroClass) {
            case "Warrior": hero = new Warrior(name); break;
            case "Mage":    hero = new Mage(name);    break;
            case "Archer":  hero = new Archer(name);  break;
            case "Healer":  hero = new Healer(name);  break;
            case "Rogue":   hero = new Rogue(name);   break;
            default: return;
        }
        storage.addHero(hero);
        storage.saveData(this);
        Toast.makeText(this, hero.getName() + " the " + hero.getHeroType() + " joins the academy!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void recruitHero() {
        String name = etHeroName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name for your hero!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupClass.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a class!", Toast.LENGTH_SHORT).show();
            return;
        }

        String heroClass = selectClass(selectedId);
        recruitHero(name, heroClass);
    }

    private String selectClass(int radioId) {
        if (radioId == R.id.radioWarrior) return "Warrior";
        if (radioId == R.id.radioMage)    return "Mage";
        if (radioId == R.id.radioArcher)  return "Archer";
        if (radioId == R.id.radioHealer)  return "Healer";
        if (radioId == R.id.radioRogue)   return "Rogue";
        return "";
    }
}