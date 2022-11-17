package me.noverita.thirdlegionplugin.Origins;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.List;

public interface Origin {
    String getIdentifier();
    String getAuthor();
    Material getIcon();
    List<AbilityDescriptor> getAbilityDescriptors();
    void unregisterListeners();
}
