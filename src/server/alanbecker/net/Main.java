package server.alanbecker.net;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main extends JavaPlugin {

    private Map<String, Team> teams;
    private int teamSizeLimit = 12;

    @Override
    public void onEnable() {
        teams = new HashMap<>();
        createTeams();
    }

    private void createTeams() {
        String[] colors = {"red", "blue", "green", "yellow", "orange", "purple", "gray"};
        ChatColor[] chatColors = {ChatColor.RED, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.GRAY};

        for (int i = 0; i < colors.length; i++) {
            teams.put(colors[i], new Team(colors[i], chatColors[i]));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("teams")) {
            if (args.length == 0) {
                listTeams(sender);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("join") && sender instanceof Player) {
                joinTeam((Player) sender, args[1].toLowerCase());
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid usage. Use /teams or /teams join [color].");
            }
            return true;
        }
        return false;
    }

    private void listTeams(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "Available teams:");
        for (String teamName : teams.keySet()) {
            Team team = teams.get(teamName);
            sender.sendMessage(ChatColor.GRAY + "- " + team.getColor() + teamName + ChatColor.GRAY + " (" + team.getPlayerCount() + "/" + teamSizeLimit + ")");
        }
    }

    private void joinTeam(Player player, String teamName) {
        if (teams.containsKey(teamName)) {
            Team team = teams.get(teamName);
            if (team.getPlayerCount() < teamSizeLimit) {
                for (Team otherTeam : teams.values()) {
                    otherTeam.removePlayer(player.getName());
                }
                team.addPlayer(player.getName());
                player.sendMessage(ChatColor.GREEN + "You have joined the " + team.getColor() + teamName + ChatColor.GREEN + " team.");
                player.setDisplayName(team.getColor() + player.getName() + ChatColor.RESET);
            } else {
                player.sendMessage(ChatColor.RED + "This team is full.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid team name. Use /teams to see available teams.");
        }
    }

    private class Team {
        private String name;
        private ChatColor color;
        private Set<String> players;

        public Team(String name, ChatColor color) {
            this.name = name;
            this.color = color;
            this.players = new HashSet<>();
        }

        public String getName() {
            return name;
        }

        public ChatColor getColor() {
            return color;
        }

        public Set<String> getPlayers() {
            return players;
        }

        public boolean addPlayer(String playerName) {
            return players.add(playerName);
        }

        public boolean removePlayer(String playerName) {
            return players.remove(playerName);
        }

        public boolean containsPlayer(String playerName) {
            return players.contains(playerName);
        }

        public int getPlayerCount() {
            return players.size();
        }
    }
}