package com.herocademy.model;

public abstract class Hero {

    private String name;
    private int energy;
    private int maxEnergy;
    private int experience;
    private int id;
    private String location;
    private String heroType;
    private boolean defending;

    public Hero(String name, int maxEnergy, String heroType) {
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.id = 0;
        this.location = "DORMS";
        this.heroType = heroType;
        this.defending = false;
    }

    // Train: costs 10 energy, gain 1 XP
    public String train() {
        if (energy >= 10) {
            energy -= 10;
            experience += 1;
            return name + " trained! XP: " + experience + " | Energy: " + energy + "/" + maxEnergy;
        } else {
            return name + " is too tired to train! Send them to the Dorms to recover.";
        }
    }

    // Called when hero returns to Dorms
    public void resetEnergy() {
        this.energy = this.maxEnergy;
        this.defending = false;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    // Basic attack damage = skill + experience bonus
    public int getAttackDamage() {
        return getSkill() + experience;
    }

    public String getDetails() {
        return name + " [" + heroType + "]" +
                "\nHP: " + energy + "/" + maxEnergy +
                " | Skill: " + (getSkill() + experience) +
                " | XP: " + experience;
    }

    // Each subclass must implement these
    public abstract int getSkill();
    public abstract int getResilience();
    public abstract int useSpecialAbility(Hero partner);
    public abstract String getSpecialName();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, energy); }
    public int getMaxEnergy() { return maxEnergy; }
    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getHeroType() { return heroType; }
    public void setHeroType(String heroType) { this.heroType = heroType; }
    public boolean isDefending() { return defending; }
    public void setDefending(boolean defending) { this.defending = defending; }
}