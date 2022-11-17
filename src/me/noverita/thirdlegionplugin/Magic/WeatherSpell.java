package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;

public class WeatherSpell implements CustomSpell {
    @Override
    public int getCost() {
        return 800;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        World w = e.getPlayer().getWorld();
        w.setStorm(w.isClearWeather());
        w.setWeatherDuration(2000);
    }
}
