package com.herocademy.model;

public class Archer extends Hero {

    private int arrows;

    public Archer(String name) {
        super(name, 90, "Archer");
        this.arrows = 10;
    }

    @Override
    public int getSkill() { return 7; }

    @Override
    public int getResilience() { return 2; }

    // Shoot Arrow: +3 bonus damage, uses 1 arrow; falls back if out of arrows
    @Override
    public int useSpecialAbility(Hero partner) {
        if (arrows > 0) {
            arrows--;
            return getSkill() + 3 + getExperience();
        } else {
            return getAttackDamage();
        }
    }

    @Override
    public String getSpecialName() { return "Shoot Arrow (" + arrows + " left)"; }

    public int getArrows() { return arrows; }
    public void setArrows(int arrows) { this.arrows = arrows; }
}