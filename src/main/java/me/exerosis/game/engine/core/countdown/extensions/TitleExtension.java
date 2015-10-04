package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.Title;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.reflection.data.Pair;

public abstract class TitleExtension extends CountdownExtension {
    private final Title _title;

    public TitleExtension(Countdown countdown, Game game) {
        super(game, countdown);
        _title = new Title(5, "", "", 0, 2, 0);
    }

    public abstract Pair<String, String> mod(int time);

    @Override
    public void done() {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_title, false);
    }

    @Override
    public void tick(int timeLeft) {
        Pair<String, String> display = mod(timeLeft);
        if (display != null) {
            _title.setTitle(display.getA());
            _title.setSubtitle(display.getB());
        }
    }

    @Override
    public void start(int time) {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_title, true);
    }

    @Override
    public void stop(int index) {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_title, false);
    }
}