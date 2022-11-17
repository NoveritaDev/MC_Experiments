package me.noverita.thirdlegionplugin.Magic;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicManager implements Listener, CommandExecutor, TabCompleter {
    private final Map<String,CustomSpell> spells;
    private final Map<Player,Integer> mana;
    private final Map<Player,Integer> maxMana;

    public MagicManager(JavaPlugin plugin) {
        mana = new HashMap<>();
        for (Player p: Bukkit.getOnlinePlayers()) {
            mana.put(p,1000);
        }
        maxMana = new HashMap<>();
        for (Player p: Bukkit.getOnlinePlayers()) {
            maxMana.put(p,1000);
        }

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player p: Bukkit.getOnlinePlayers()) {
                int current = mana.get(p);
                int max = maxMana.get(p);
                if (current < max) {
                    mana.put(p,current + 1);
                }
                TextComponent text = new TextComponent(String.format("%d/%d mana",current + 1, max));
                text.setColor(ChatColor.LIGHT_PURPLE);
                p.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        text
                );
            }
        }, 0, 20);

        spells = new HashMap<>();
        spells.put("Fireball I",new FireballSpell(1));
        spells.put("Fireball II",new FireballSpell(1.5f));
        spells.put("Fireball III",new FireballSpell(2f));
        spells.put("Fireball IV",new FireballSpell(2.5f));
        spells.put("Fireball V",new FireballSpell(3f));
        spells.put("Nuclear Strike",new FireballSpell(25f));
        spells.put("Annihilation",new AnnihilationSpell(10));
        spells.put("Lightning I",new LightningSpell(0));
        spells.put("Lightning II",new LightningSpell(1));
        spells.put("Lightning III",new LightningSpell(2));
        spells.put("Jump I",new JumpSpell(0.5));
        spells.put("Jump II",new JumpSpell(1));
        spells.put("Jump III",new JumpSpell(2));
        spells.put("Jump IV",new JumpSpell(4));
        spells.put("Jump V",new JumpSpell(8));
        spells.put("Shockwave",new ShockwaveSpell());
        spells.put("Lightning Storm I", new LightningStorm(3));
        spells.put("Lightning Storm II", new LightningStorm(7));
        spells.put("Lightning Storm III", new LightningStorm(15));
        spells.put("Napalm", new NapalmSpell());
        spells.put("Ice Orb I", new IceOrbSpell(2, plugin));
        spells.put("Ice Orb II", new IceOrbSpell(4, plugin));
        spells.put("Ice Orb III", new IceOrbSpell(6, plugin));
        spells.put("Oakskin", new SelfPotionEffectSpell(PotionEffectType.DAMAGE_RESISTANCE, 400, true, 30, 0));
        spells.put("Stoneskin", new SelfPotionEffectSpell(PotionEffectType.DAMAGE_RESISTANCE, 400, true, 60, 1));
        spells.put("Ironskin", new SelfPotionEffectSpell(PotionEffectType.DAMAGE_RESISTANCE, 400, true, 120, 2));
        spells.put("Stoneblast", new StoneblastSpell());
        spells.put("Spire", new SpireSpell());
        spells.put("Faefire", new SelfPotionEffectSpell(PotionEffectType.NIGHT_VISION, 6000, true, 40, 0));
        spells.put("Darkness", new DarknessSpell());
        spells.put("Black Hole", new BlackHoleSpell(plugin));
        spells.put("Poison Cloud I", new PoisonCloudSpell(1));
        spells.put("Poison Cloud II", new PoisonCloudSpell(2));
        spells.put("Poison Cloud III", new PoisonCloudSpell(4));
        spells.put("Invisibility", new SelfPotionEffectSpell(PotionEffectType.INVISIBILITY, 1200, true, 300, 0));
        spells.put("Greater Invisibility", new SelfPotionEffectSpell(PotionEffectType.INVISIBILITY, 1200, false, 300, 0));
        spells.put("Speed I", new SelfPotionEffectSpell(PotionEffectType.SPEED, 1200, true, 50, 0));
        spells.put("Speed II", new SelfPotionEffectSpell(PotionEffectType.SPEED, 1200, true, 50, 1));
        spells.put("Speed III", new SelfPotionEffectSpell(PotionEffectType.SPEED, 1200, true, 50, 2));
        spells.put("Swift Swim I", new SelfPotionEffectSpell(PotionEffectType.DOLPHINS_GRACE, 1200, true, 50, 0));
        spells.put("Swift Swim II", new SelfPotionEffectSpell(PotionEffectType.DOLPHINS_GRACE, 1200, true, 100, 1));
        spells.put("Swift Swim III", new SelfPotionEffectSpell(PotionEffectType.DOLPHINS_GRACE, 1200, true, 200, 2));
        spells.put("Summon Food", new SummonFoodSpell());
        spells.put("Heal I", new SelfPotionEffectSpell(PotionEffectType.REGENERATION, 100, true, 50, 0));
        spells.put("Heal II", new SelfPotionEffectSpell(PotionEffectType.REGENERATION, 100, true, 50, 1));
        spells.put("Heal III", new SelfPotionEffectSpell(PotionEffectType.REGENERATION, 100, true, 50, 2));
        spells.put("Glyph of Warding", new WardingSpell(plugin));
        spells.put("Repair", new RepairSpell());
        spells.put("Teleport", new TeleportSpell(16));
        spells.put("Mid-range Teleport", new TeleportSpell(32));
        spells.put("Long Teleport", new TeleportSpell(48));
        spells.put("Change Weather", new WeatherSpell());
        spells.put("Summon Horse I", new SummonHorseSpell(0.4,0.7,15));
        spells.put("Summon Horse II", new SummonHorseSpell(0.6,1.0,22));
        spells.put("Summon Horse III", new SummonHorseSpell(0.8,1.3,30));
        spells.put("Charge I", new ChargeSpell(1));
        spells.put("Charge II", new ChargeSpell(2));
        spells.put("Charge III", new ChargeSpell(3));
        spells.put("Lightning Wall",new WallOfLightningSpell());
        spells.put("Firestorm I",new FallingBlockStormSpell(10, Material.FIRE));
        spells.put("Firestorm II",new FallingBlockStormSpell(20, Material.FIRE));
        spells.put("Firestorm III",new FallingBlockStormSpell(40, Material.FIRE));
        spells.put("Hailstorm I",new FallingBlockStormSpell(10, Material.BLUE_ICE));
        spells.put("Hailstorm II",new FallingBlockStormSpell(20, Material.BLUE_ICE));
        spells.put("Hailstorm III",new FallingBlockStormSpell(40, Material.BLUE_ICE));
        spells.put("Blight I",new BlightSpell(5,plugin));
        spells.put("Blight II",new BlightSpell(10,plugin));
        spells.put("Blight III",new BlightSpell(20,plugin));
        spells.put("Recall",new Recall());
        spells.put("Growth I",new GrowthSpell(5));
        spells.put("Growth II",new GrowthSpell(10));
        spells.put("Growth III",new GrowthSpell(20));
        spells.put("Beam of Light I",new BeamOfLightSpell(5,plugin));
        spells.put("Beam of Light II",new BeamOfLightSpell(10,plugin));
        spells.put("Beam of Light III",new BeamOfLightSpell(15,plugin));
        spells.put("Sunder",new SunderSpell());
        spells.put("Drain I",new HealthDrainSpell(1));
        spells.put("Drain II",new HealthDrainSpell(2));
        spells.put("Drain III",new HealthDrainSpell(3));
        spells.put("Drain IV",new HealthDrainSpell(4));
        spells.put("Drain V",new HealthDrainSpell(5));
        spells.put("Earthen Wall", new WallSpell());
        spells.put("Protection from Mobs", new MobNoAggroSpell(plugin));
        spells.put("Heal Other I", new HealSpell(1));
        spells.put("Heal Other II", new HealSpell(2));
        spells.put("Heal Other III", new HealSpell(3));
        spells.put("Heal Other IV", new HealSpell(4));
        spells.put("Heal Other V", new HealSpell(5));

        // Ice Wall
        // Slowness Aura
        // Sap Food
        // Cone of {Element}
        // Confusion aura
        // Scatter Players
        // Transposition
        // Phantom Sounds
        // Area/Ally Targeted Buffs?
        // Antidote
        // Freedom of Movement
        // Paralysis
        // Single heavy damage attack (holy judgement or something)
        // Blood sacrifice to do... Something
        // Arrow Targeting - Make arrows veer towards the target.
        // Miner's Dream - Remove stone in a 3x3 when mining, for some amount of time.
        // Protection from Arrows
        // Gust - Knockback in direction that the player is facing.
        // Fire Aura
        // Curse / Remove Curse
        // Hallow / Desecrate
        // Entangle
        // Antimagic Area
        // Attract Item
        // Spider Climb
        // Reverse Gravity
        // Shield other - Take damage for another person.
        // {Builders wand by some other name}
        // Alarm
        // Detect treasure - Alert if near a chest
        // Remote viewing
        // Harvest
        // Lifelink - If one party dies, the other does as well.
        // Wall of force - temporary impenetrable wall
        // Steal - Steal a targets held item
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        mana.put(event.getPlayer(),1000);
        maxMana.put(event.getPlayer(),1000);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        mana.remove(event.getPlayer());
        maxMana.remove(event.getPlayer());
    }

    @EventHandler
    public void manaPotion(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (event.getItem().getType() == Material.POTION) {
            PotionType type = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType();
            if (type == PotionType.WATER && item.getItemMeta().getDisplayName().equals("Mana Potion")) {
                Player p = event.getPlayer();
                mana.put(p,Math.min(maxMana.get(p), mana.get(p) + 200));
            }
        }
    }

    @EventHandler
    public void onCast(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player p = event.getPlayer();
        if (item != null
                && item.getType() == Material.ENCHANTED_BOOK
                && item.getItemMeta() != null
                && item.getItemMeta().getLore() != null) {
            for (String s: item.getItemMeta().getLore()) {

                if (spells.containsKey(s.strip())) {
                    event.setCancelled(true);
                    CustomSpell spell = spells.get(s);
                    int currentMana = mana.get(p);

                    if (currentMana >= spell.getCost()) {
                        spell.execute(event);

                        mana.put(p, currentMana - spell.getCost());
                        TextComponent text = new TextComponent(String.format("%d/%d mana", mana.get(p), maxMana.get(p)));
                        text.setColor(ChatColor.LIGHT_PURPLE);
                        p.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                text
                        );
                    } else {
                        TextComponent text = new TextComponent(String.format("%d/%d mana", mana.get(p), maxMana.get(p)));
                        text.setColor(ChatColor.RED);
                        p.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                text
                        );
                    }
                }
            }
        }
    }

    @EventHandler
    public void getSpells(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.NETHER_STAR) {
            //item.setAmount(item.getAmount() - 1);
            for (String s: spells.keySet()) {
                ItemStack tome = new ItemStack(Material.ENCHANTED_BOOK,1);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(List.of(s));
                meta.setDisplayName(s);
                tome.setItemMeta(meta);
                event.getPlayer().getInventory().addItem(tome);
            }
            ItemStack tome = new ItemStack(Material.ENCHANTED_BOOK,1);
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> spellStrings = new ArrayList<>();
            spellStrings.addAll(spells.keySet());
            meta.setLore(spellStrings);
            meta.setDisplayName("Multispell");
            tome.setItemMeta(meta);
            event.getPlayer().getInventory().addItem(tome);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length > 0) {
                Player p = (Player) commandSender;
                String name = String.join(" ", strings);
                if (!spells.containsKey(name)) {
                    return false;
                }
                ItemStack tome = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta meta = tome.getItemMeta();
                meta.setLore(List.of(name));
                meta.setDisplayName(name);
                tome.setItemMeta(meta);
                p.getInventory().addItem(tome);
            } else {
                String spellsString = ChatColor.LIGHT_PURPLE + String.join(", ",spells.keySet()) + ChatColor.RESET;
                Player p = (Player) commandSender;
                p.sendMessage(spellsString);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0 || strings.length == 1 && strings[0].equals("")) {
            ArrayList<String> temp = new ArrayList<>(spells.keySet());
            return temp;
        } else {
            String name = String.join(" ", strings);
            ArrayList<String> temp = new ArrayList<>();
            for (String key: spells.keySet()) {
                if (key.startsWith(name)) {
                    temp.add(key);
                }
            }
            return temp;
        }
    }
}
