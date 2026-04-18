package com.herocademy.model;

public class Warrior extends Hero {

    private int armor;

    public Warrior(String name) {
        super(name, 100, "Warrior");
        this.armor = 3;
    }

    @Override
    public int getSkill() { return 6; }

    @Override
    public int getResilience() { return 4 + armor; }

    // Shield Block: boosts armor permanently for this battle, returns normal damage
    @Override
    public int useSpecialAbility(Hero partner) {
        armor += 2;
        return getAttackDamage();
    }

    @Override
    public String getSpecialName() { return "Shield Block"; }

    public void shieldBlock() { armor += 2; }
    public int getArmor() { return armor; }
    public void setArmor(int armor) { this.armor = armor; }
}