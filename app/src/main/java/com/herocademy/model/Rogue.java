package com.herocademy.model;

public class Rogue extends Hero {

    private int stealth;

    public Rogue(String name) {
        super(name, 85, "Rogue");
        this.stealth = 6;
    }

    @Override
    public int getSkill() { return 8; }

    @Override
    public int getResilience() { return 0; }

    // Backstab: highest single-hit damage using stealth bonus
    @Override
    public int useSpecialAbility(Hero partner) {
        return getSkill() + stealth + getExperience();
    }

    @Override
    public String getSpecialName() { return "Backstab"; }

    public int backstab() { return getSkill() + stealth + getExperience(); }
    public int getStealth() { return stealth; }
    public void setStealth(int stealth) { this.stealth = stealth; }
}