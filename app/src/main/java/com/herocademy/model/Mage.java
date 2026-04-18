package com.herocademy.model;

public class Mage extends Hero {

    private int mana;

    public Mage(String name) {
        super(name, 80, "Mage");
        this.mana = 5;
    }

    @Override
    public int getSkill() { return 9; }

    @Override
    public int getResilience() { return 1; }

    // Cast Spell: uses mana for double damage; falls back to normal if no mana
    @Override
    public int useSpecialAbility(Hero partner) {
        if (mana > 0) {
            mana--;
            return getSkill() * 2 + getExperience();
        } else {
            return getAttackDamage();
        }
    }

    @Override
    public String getSpecialName() { return "Cast Spell (" + mana + " mana)"; }

    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = mana; }
}