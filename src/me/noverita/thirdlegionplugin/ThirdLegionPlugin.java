package me.noverita.thirdlegionplugin;

import me.noverita.thirdlegionplugin.Magic.MagicManager;
import me.noverita.thirdlegionplugin.Origins.Implementations.Slimeling;
import me.noverita.thirdlegionplugin.Origins.OriginHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

// TODO:
// Severe storms
// Tunneling mobs


public class ThirdLegionPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());

        PluginManager pm = this.getServer().getPluginManager();

        CommandMercy mercy = new CommandMercy();
        this.getCommand("mercy").setExecutor(mercy);
        pm.registerEvents(mercy, this);

        BossAbilities secret = new BossAbilities();
        this.getCommand("secret").setExecutor(secret);
        pm.registerEvents(secret, this);

        //this.getCommand("purify").setExecutor(new PurificationAnimation(this));

        pm.registerEvents(new MessengerBirds(this), this);
        pm.registerEvents(new FancyShieldWall(), this);
        //pm.registerEvents(new DeathAnimation(this), this);
        pm.registerEvents(new RealisticFalls(), this);
        pm.registerEvents(new LocalDeathMessages(), this);
        //pm.registerEvents(new Thirst(this), this);
        //pm.registerEvents(new BleedingHandler(this), this);
        //pm.registerEvents(new HarderMobs(), this);
        //pm.registerEvents(new NutritionalDiversity(), this);
        pm.registerEvents(new LocalCrops(), this);
        //pm.registerEvents(new AltitudeSickness(), this);
        //pm.registerEvents(new HideBehindWalls(this),this);
        //pm.registerEvents(new FakeNameHandler(), this);
        pm.registerEvents(new DeathStages(this), this);

        //pm.registerEvents(new DualWielding(),this);

        pm.registerEvents(new RideEverything(this),this);

        getLogger().info("Loading Magic Manager");
        MagicManager mm = new MagicManager(this);
        getCommand("getspell").setExecutor(mm);
        getCommand("getspell").setTabCompleter(mm);
        pm.registerEvents(mm,this);
        getLogger().info("Loaded Magic Manager");

        // Prototype Origins
        //OriginHandler originHandler = new OriginHandler();
        //pm.registerEvents(originHandler,this);
        //OriginHandler.register(new Slimeling(this));


    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }
}
