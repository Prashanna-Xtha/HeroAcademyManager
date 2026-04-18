package com.herocademy.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.herocademy.model.Archer;
import com.herocademy.model.Healer;
import com.herocademy.model.Hero;
import com.herocademy.model.Mage;
import com.herocademy.model.Rogue;
import com.herocademy.model.Warrior;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeroStorage {

    private static HeroStorage instance;
    private HashMap<Integer, Hero> heroes;
    private int nextId;
    private int questCount;

    private static final String PREFS_NAME = "HeroAcademyData";
    private static final String KEY_HEROES = "heroes_json";
    private static final String KEY_NEXT_ID = "next_id";
    private static final String KEY_QUEST_COUNT = "quest_count";

    private HeroStorage() {
        heroes = new HashMap<>();
        nextId = 1;
        questCount = 0;
    }

    // Singleton: only one instance ever exists
    public static HeroStorage getInstance() {
        if (instance == null) {
            instance = new HeroStorage();
        }
        return instance;
    }

    public void addHero(Hero hero) {
        hero.setId(nextId);
        heroes.put(nextId, hero);
        nextId++;
    }

    public void removeHero(int id) {
        heroes.remove(id);
    }

    public Hero getHero(int id) {
        return heroes.get(id);
    }

    public List<Hero> getAllHeroes() {
        return new ArrayList<>(heroes.values());
    }

    public List<Hero> getHeroesByLocation(String location) {
        List<Hero> result = new ArrayList<>();
        for (Hero hero : heroes.values()) {
            if (location.equals(hero.getLocation())) {
                result.add(hero);
            }
        }
        return result;
    }

    public int getQuestCount() { return questCount; }
    public void incrementQuestCount() { questCount++; }

    // Save all heroes to device storage
    public void saveData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        JsonArray array = new JsonArray();

        for (Hero hero : heroes.values()) {
            JsonObject obj = gson.toJsonTree(hero).getAsJsonObject();
            obj.addProperty("heroType", hero.getHeroType());
            array.add(obj);
        }

        prefs.edit()
                .putString(KEY_HEROES, array.toString())
                .putInt(KEY_NEXT_ID, nextId)
                .putInt(KEY_QUEST_COUNT, questCount)
                .apply();
    }

    // Load all heroes from device storage
    public void loadData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_HEROES, null);
        nextId = prefs.getInt(KEY_NEXT_ID, 1);
        questCount = prefs.getInt(KEY_QUEST_COUNT, 0);

        heroes.clear();
        if (json == null) return;

        Gson gson = new Gson();
        JsonArray array = JsonParser.parseString(json).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            String type = obj.get("heroType").getAsString();
            Hero hero;

            switch (type) {
                case "Warrior": hero = gson.fromJson(obj, Warrior.class); break;
                case "Mage":    hero = gson.fromJson(obj, Mage.class);    break;
                case "Archer":  hero = gson.fromJson(obj, Archer.class);  break;
                case "Healer":  hero = gson.fromJson(obj, Healer.class);  break;
                case "Rogue":   hero = gson.fromJson(obj, Rogue.class);   break;
                default: continue;
            }

            hero.setDefending(false); // reset any combat state on load
            heroes.put(hero.getId(), hero);
        }
    }
}