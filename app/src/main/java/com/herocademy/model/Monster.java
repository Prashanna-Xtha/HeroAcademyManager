package com.herocademy.model;

public class Monster {

    private String name;
    private int skill;
    private int resilience;
    private int energy;
    private int maxEnergy;

    public Monster(String name, int skill, int resilience, int maxEnergy) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public boolean isDefeated() {
        return energy <= 0;
    }

    // Generate a monster that scales with how many quests have been done
    public static Monster generateMonster(int questCount) {
        String[] names = {"Goblin Scout", "Shadow Wolf", "Stone Golem", "Flame Drake", "Lich Knight"};
        String name = names[questCount % names.length];
        int skill = 5 + questCount;
        int res = questCount / 2;
        int maxEnergy = 25 + (questCount * 8);
        return new Monster(name, skill, res, maxEnergy);
    }

    // Getters and setters
    public String getName() { return name; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, energy); }
    public int getMaxEnergy() { return maxEnergy; }
}