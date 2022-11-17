package me.noverita.thirdlegionplugin.Origins;

public class AbilityDescriptor {
    public enum AbilityType {
        POSITIVE,
        NEGATIVE,
        EXTRA
    }

    private String name;
    private String description;
    private AbilityType abilityType;

    public AbilityDescriptor(String name, String description, AbilityType abilityType) {
        this.name = name;
        this.description = description;
        this.abilityType = abilityType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }
}
