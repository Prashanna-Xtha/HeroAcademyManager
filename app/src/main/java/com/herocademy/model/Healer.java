package com.herocademy.model;

public class Healer extends Hero {

    private int faith;

    public Healer(String name) {
        super(name, 110, "Healer");
        this.faith = 4;
    }

    @Override
    public int getSkill() { return 5; }

    @Override
    public int getResilience() { return 3; }

    // Heal Partner: restores energy to partner instead of attacking; returns 0 damage to monster
    @Override
    public int useSpecialAbility(Hero partner) {
        int healAmount = 15 + faith;
        int newEnergy = Math.min(partner.getEnergy() + healAmount, partner.getMaxEnergy());
        partner.setEnergy(newEnergy);
        return 0;
    }

    @Override
    public String getSpecialName() { return "Heal Partner"; }

    public void heal(Hero target) {
        int healAmount = 15 + faith;
        int newEnergy = Math.min(target.getEnergy() + healAmount, target.getMaxEnergy());
        target.setEnergy(newEnergy);
    }

    public int getFaith() { return faith; }
    public void setFaith(int faith) { this.faith = faith; }
}