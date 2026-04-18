package com.herocademy;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herocademy.model.Hero;
import com.herocademy.model.Monster;
import com.herocademy.storage.HeroStorage;

import java.util.List;

public class QuestActivity extends AppCompatActivity {

    private Spinner spinnerHeroA, spinnerHeroB;
    private TextView tvBattleLog, tvCurrentTurn;
    private LinearLayout layoutSelection, layoutCombatActions;
    private ScrollView scrollBattleLog;
    private Button btnLaunchQuest, btnAttack, btnDefend, btnSpecial;

    private HeroStorage storage;
    private Hero heroA, heroB;
    private Hero currentHero, partnerHero;
    private Monster currentMonster;
    private boolean questInProgress = false;
    private StringBuilder battleLog;
    private List<Hero> questHeroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        storage = HeroStorage.getInstance();

        spinnerHeroA        = findViewById(R.id.spinnerHeroA);
        spinnerHeroB        = findViewById(R.id.spinnerHeroB);
        tvBattleLog         = findViewById(R.id.tvBattleLog);
        tvCurrentTurn       = findViewById(R.id.tvCurrentTurn);
        layoutSelection     = findViewById(R.id.layoutSelection);
        layoutCombatActions = findViewById(R.id.layoutCombatActions);
        scrollBattleLog     = findViewById(R.id.scrollBattleLog);
        btnLaunchQuest      = findViewById(R.id.btnLaunchQuest);
        btnAttack           = findViewById(R.id.btnAttack);
        btnDefend           = findViewById(R.id.btnDefend);
        btnSpecial          = findViewById(R.id.btnSpecial);

        btnLaunchQuest.setOnClickListener(v -> startQuest());
        btnAttack.setOnClickListener(v      -> processTurn("ATTACK"));
        btnDefend.setOnClickListener(v      -> processTurn("DEFEND"));
        btnSpecial.setOnClickListener(v     -> processTurn("SPECIAL"));

        loadHeroSpinners();
    }

    private void loadHeroSpinners() {
        questHeroes = storage.getHeroesByLocation("QUEST");

        if (questHeroes.isEmpty()) {
            tvBattleLog.setText("No heroes on the Quest Board.\n\nGo to Dorms and move heroes here first.");
            btnLaunchQuest.setEnabled(false);
            return;
        }

        btnLaunchQuest.setEnabled(true);
        String[] heroNames = new String[questHeroes.size()];
        for (int i = 0; i < questHeroes.size(); i++) {
            Hero h = questHeroes.get(i);
            heroNames[i] = h.getName() + " [" + h.getHeroType() + "]  HP:" + h.getEnergy() + "  XP:" + h.getExperience();
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heroNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeroA.setAdapter(spinnerAdapter);
        spinnerHeroB.setAdapter(spinnerAdapter);

        if (questHeroes.size() > 1) {
            spinnerHeroB.setSelection(1);
        }
    }

    private void startQuest() {
        questHeroes = storage.getHeroesByLocation("QUEST");

        if (questHeroes.size() < 2) {
            Toast.makeText(this, "You need at least 2 heroes on the Quest Board!", Toast.LENGTH_SHORT).show();
            return;
        }

        int posA = spinnerHeroA.getSelectedItemPosition();
        int posB = spinnerHeroB.getSelectedItemPosition();

        if (posA == posB) {
            Toast.makeText(this, "Select two different heroes!", Toast.LENGTH_SHORT).show();
            return;
        }

        heroA = questHeroes.get(posA);
        heroB = questHeroes.get(posB);
        currentHero = heroA;
        partnerHero = heroB;

        currentMonster = Monster.generateMonster(storage.getQuestCount());
        battleLog = new StringBuilder();
        questInProgress = true;

        layoutSelection.setVisibility(View.GONE);
        layoutCombatActions.setVisibility(View.VISIBLE);

        battleLog.append("=== QUEST #").append(storage.getQuestCount() + 1).append(" STARTED ===\n\n");
        battleLog.append("Monster: ").append(currentMonster.getName())
                .append("  Skill:").append(currentMonster.getSkill())
                .append("  Res:").append(currentMonster.getResilience())
                .append("  HP:").append(currentMonster.getEnergy()).append("/").append(currentMonster.getMaxEnergy()).append("\n\n");
        battleLog.append("Hero A: ").append(heroA.getName()).append(" [").append(heroA.getHeroType()).append("]")
                .append("  Skill:").append(heroA.getSkill() + heroA.getExperience())
                .append("  Res:").append(heroA.getResilience())
                .append("  HP:").append(heroA.getEnergy()).append("/").append(heroA.getMaxEnergy()).append("\n");
        battleLog.append("Hero B: ").append(heroB.getName()).append(" [").append(heroB.getHeroType()).append("]")
                .append("  Skill:").append(heroB.getSkill() + heroB.getExperience())
                .append("  Res:").append(heroB.getResilience())
                .append("  HP:").append(heroB.getEnergy()).append("/").append(heroB.getMaxEnergy()).append("\n\n");
        battleLog.append("--- Round begins ---\n\n");

        updateBattleLog();
        updateTurnLabel();
    }

    private void processTurn(String action) {
        if (!questInProgress) return;

        setActionButtonsEnabled(false);

        // STEP 1: Hero acts against monster
        int rawHeroDamage;
        String actionDesc;

        switch (action) {
            case "ATTACK":
                rawHeroDamage = currentHero.getAttackDamage();
                actionDesc = currentHero.getName() + " attacks!";
                break;
            case "DEFEND":
                currentHero.setDefending(true);
                rawHeroDamage = currentHero.getAttackDamage();
                actionDesc = currentHero.getName() + " defends and attacks!";
                break;
            case "SPECIAL":
            default:
                rawHeroDamage = currentHero.useSpecialAbility(partnerHero);
                actionDesc = currentHero.getName() + " uses " + currentHero.getSpecialName() + "!";
                break;
        }

        battleLog.append(actionDesc).append("\n");

        if (rawHeroDamage == 0) {
            // Healer used heal — no damage to monster
            battleLog.append("  ").append(partnerHero.getName()).append(" healed!  HP: ")
                    .append(partnerHero.getEnergy()).append("/").append(partnerHero.getMaxEnergy()).append("\n");
        } else {
            int monsterActualDamage = Math.max(1, rawHeroDamage - currentMonster.getResilience());
            currentMonster.setEnergy(Math.max(0, currentMonster.getEnergy() - monsterActualDamage));
            battleLog.append("  Damage: ").append(rawHeroDamage).append(" - ").append(currentMonster.getResilience())
                    .append(" res = ").append(monsterActualDamage).append("\n");
            battleLog.append("  ").append(currentMonster.getName()).append(" HP: ")
                    .append(currentMonster.getEnergy()).append("/").append(currentMonster.getMaxEnergy()).append("\n");
        }

        // Check if monster is dead
        if (currentMonster.isDefeated()) {
            endQuest(true);
            return;
        }

        // STEP 2: Monster retaliates
        int heroRes = currentHero.isDefending() ? currentHero.getResilience() * 2 : currentHero.getResilience();
        currentHero.setDefending(false); // reset defending flag
        int rawMonsterDamage = currentMonster.getSkill();
        int heroActualDamage = Math.max(1, rawMonsterDamage - heroRes);
        currentHero.setEnergy(Math.max(0, currentHero.getEnergy() - heroActualDamage));

        battleLog.append(currentMonster.getName()).append(" retaliates against ").append(currentHero.getName()).append("!\n");
        battleLog.append("  Damage: ").append(rawMonsterDamage).append(" - ").append(heroRes)
                .append(" res = ").append(heroActualDamage).append("\n");
        battleLog.append("  ").append(currentHero.getName()).append(" HP: ")
                .append(currentHero.getEnergy()).append("/").append(currentHero.getMaxEnergy()).append("\n\n");

        // STEP 3: Check if current hero died
        if (currentHero.isDead()) {
            battleLog.append("*** ").append(currentHero.getName()).append(" has fallen! Removed from the academy. ***\n\n");
            storage.removeHero(currentHero.getId());

            Hero survivor = (currentHero == heroA) ? heroB : heroA;

            if (survivor.isDead()) {
                storage.saveData(this);
                endQuest(false);
                return;
            }

            // Surviving hero continues alone
            currentHero = survivor;
            partnerHero = survivor; // fighting solo
            storage.saveData(this);
            battleLog.append(currentHero.getName()).append(" fights on alone!\n\n");
            updateBattleLog();
            updateTurnLabel();
            btnSpecial.setText(currentHero.getSpecialName());
            setActionButtonsEnabled(true);
            return;
        }

        // STEP 4: Switch to other hero if alive
        Hero nextHero = (currentHero == heroA) ? heroB : heroA;
        if (!nextHero.isDead()) {
            Hero temp = currentHero;
            currentHero = nextHero;
            partnerHero = temp;
        }
        // If next hero is dead, same hero continues (solo)

        updateBattleLog();
        updateTurnLabel();
        btnSpecial.setText(currentHero.getSpecialName());
        setActionButtonsEnabled(true);
    }

    private void endQuest(boolean victory) {
        questInProgress = false;
        layoutCombatActions.setVisibility(View.GONE);
        layoutSelection.setVisibility(View.VISIBLE);

        if (victory) {
            battleLog.append("=== QUEST COMPLETE ===\n");
            battleLog.append("The ").append(currentMonster.getName()).append(" has been defeated!\n\n");

            if (!heroA.isDead()) {
                heroA.setExperience(heroA.getExperience() + 1);
                battleLog.append(heroA.getName()).append(" gains 1 XP! (Total: ").append(heroA.getExperience()).append(")\n");
            }
            if (!heroB.isDead()) {
                heroB.setExperience(heroB.getExperience() + 1);
                battleLog.append(heroB.getName()).append(" gains 1 XP! (Total: ").append(heroB.getExperience()).append(")\n");
            }

            storage.incrementQuestCount();
            battleLog.append("\nSend your heroes to the Dorms to recover energy.\n");
            battleLog.append("Next monster will be stronger! (Quest #").append(storage.getQuestCount() + 1).append(")\n");
        } else {
            battleLog.append("=== QUEST FAILED ===\n");
            battleLog.append("All heroes have fallen. The academy mourns their loss.\n");
        }

        storage.saveData(this);
        updateBattleLog();
        loadHeroSpinners(); // refresh spinners
    }

    private void updateBattleLog() {
        tvBattleLog.setText(battleLog.toString());
        scrollBattleLog.post(() -> scrollBattleLog.fullScroll(View.FOCUS_DOWN));
    }

    private void updateTurnLabel() {
        if (currentHero != null) {
            tvCurrentTurn.setText(currentHero.getName() + "'s turn — choose an action:");
        }
    }

    private void setActionButtonsEnabled(boolean enabled) {
        btnAttack.setEnabled(enabled);
        btnDefend.setEnabled(enabled);
        btnSpecial.setEnabled(enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!questInProgress) {
            loadHeroSpinners();
        }
    }
}