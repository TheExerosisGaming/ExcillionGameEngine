package me.exerosis.game.engine.componentgame.component.core.scoreboard;

import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public class Scoreboard implements Runnable {
    int _index = 0;
    private int _scoreValue = 99;
    private int _blankValue;
    private org.bukkit.scoreboard.Scoreboard _scoreboard;
    private Objective _objective;
    private ChatColor _currentColor = ChatColor.RED;

    public Scoreboard(String name) {
        _scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        _objective = _scoreboard.registerNewObjective(name, "dummy");
        _objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        ExTask.startTask(this, 20, 10);
    }

    public String addBlank() {
        String id = "blank" + _blankValue++;
        addLine(" ", id);
        return id;
    }

    public void addLine(String line, String id) {
        if (line.length() > 16)
            throw new IllegalArgumentException("Line too long: " + line);
        Team team = _scoreboard.registerNewTeam(id);
        team.setSuffix(line);
        String name = getNextEntryName();
        team.addEntry(name);
        _objective.getScore(name).setScore(_scoreValue--);
    }

    public void editLine(String line, String id) {
        Team team = _scoreboard.getTeam(id);
        if (team != null)
            team.setSuffix(line);
        else
            addLine(line, id);
    }

    public void removeLine(String id) {
        _scoreboard.getTeam(id).unregister();
    }

    public void showTo(Player player) {
        player.setScoreboard(_scoreboard);
    }

    @Override
    public void run() {
        for (Team team : _scoreboard.getTeams())
            team.setPrefix(getNextColor());
    }

    private String getNextColor() {
        if (_currentColor.equals(ChatColor.RED))
            _currentColor = ChatColor.BLUE;
        else
            _currentColor = ChatColor.RED;
        return _currentColor.toString();
    }

    private String getNextEntryName() {
        StringBuilder nextEntry = new StringBuilder();
        ChatColor[] values = ChatColor.values();
        int left = _index % (values.length - 1);
        int times = (_index - left) / (values.length - 1);

        if (times > 0) {
            if (times > 3)
                throw new RuntimeException("Not enough ChatColors left, too many lines!");
            else if (times == 1)
                nextEntry.append(ChatColor.BOLD);
            else if (times == 2)
                nextEntry.append(ChatColor.UNDERLINE);
        }
        _index++;
        return nextEntry.append(values[left]).toString();
    }

    //Getters and setters.
    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return _scoreboard;
    }

    public Objective getObjective() {
        return _objective;
    }
}
