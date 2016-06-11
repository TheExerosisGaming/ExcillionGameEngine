package me.exerosis.game.engine.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardImplementation implements Runnable {
    private final Objective objective;
    private final BukkitTask task;
    private final Scoreboard scoreboard;
        
    private int index = 0;
    private int scoreValue = 99;
    private int blankValue;
    private ChatColor currentColor = ChatColor.RED;

    public Scoreboard(String name, Plugin plugin) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 0);
    }

    public String addBlank() {
        String id = "blank" + blankValue++;
        addLine(" ", id);
        return id;
    }

    public void addLine(String line, String id) {
        if (line.length() > 16)
            throw new IllegalArgumentException("Line too long: " + line);
        Team team = scoreboard.registerNewTeam(id);
        team.setSuffix(line);
        String name = getNextEntryName();
        team.addEntry(name);
        objective.getScore(name).setScore(scoreValue--);
    }

    public void editLine(String line, String id) {
        Team team = scoreboard.getTeam(id);
        if (team != null)
            team.setSuffix(line);
        else
            addLine(line, id);
    }

    public void removeLine(String id) {
        scoreboard.getTeam(id).unregister();
    }

    public void showTo(Player player) {
        player.setScoreboard(scoreboard);
    }

    @Override
    public void run() {
        for (Team team : scoreboard.getTeams())
            team.setPrefix(getNextColor());
    }

    private String getNextColor() {
        if (currentColor.equals(ChatColor.RED))
            currentColor = ChatColor.BLUE;
        else
            currentColor = ChatColor.RED;
        return currentColor.toString();
    }

    private String getNextEntryName() {
        StringBuilder nextEntry = new StringBuilder();
        ChatColor[] values = ChatColor.values();
        int left = index % (values.length - 1);
        int times = (index - left) / (values.length - 1);

        if (times > 0) {
            if (times > 3)
                throw new RuntimeException("Not enough ChatColors left, too many lines!");
            else if (times == 1)
                nextEntry.append(ChatColor.BOLD);
            else if (times == 2)
                nextEntry.append(ChatColor.UNDERLINE);
        }
        index++;
        return nextEntry.append(values[left]).toString();
    }
    
    public void shutdown(){
        task.cancel();
    }

    //Getters and setters.
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }
}
