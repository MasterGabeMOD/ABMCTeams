package server.alanbecker.net;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
    private Main plugin;
    private File playerDataFile;
    private FileConfiguration playerData;

    public PlayerData(Main plugin) {
        this.plugin = plugin;
        setupPlayerDataFile();
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private void setupPlayerDataFile() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        
        playerDataFile = new File(dataFolder, "playerdata.yml");
        
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create playerdata.yml");
                e.printStackTrace();
            }
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    }


    public void addPlayerToTeam(Player player, String teamName) {
        playerData.set(player.getUniqueId().toString(), teamName);
        savePlayerData();
    }

    public void removePlayerFromTeam(Player player, String teamName) {
        if (teamName.equals(playerData.getString(player.getUniqueId().toString()))) {
            playerData.set(player.getUniqueId().toString(), null);
            savePlayerData();
        }
    }
    
    public Set<String> getPlayersInTeam(String teamName) {
        Set<String> playersInTeam = new HashSet<>();

        for (String uuid : playerData.getKeys(false)) {
            if (teamName.equalsIgnoreCase(playerData.getString(uuid))) {
                String playerName = plugin.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName();
                if (playerName != null) {
                    playersInTeam.add(playerName);
                }
            }
        }

        return playersInTeam;
    }



    private void savePlayerData() {
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerdata.yml");
            e.printStackTrace();
          
        }
    }
}
