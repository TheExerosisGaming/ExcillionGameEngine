package me.exerosis.game.engine.util;

public class TimeUtil {
    /**
     * Credit to Mark I didn't make this #TooLazy
     *
     * @param seconds
     * @return
     */
    public static String formatTime(int seconds) {
        int hours = seconds % 2678400 % 604800 % 86400 / 3600;
        int days = seconds % 2678400 % 604800 / 86400;
        int weeks = seconds % 2678400 / 604800;
        int months = seconds / 2678400;
        int mins = seconds % 2678400 % 604800 % 86400 % 3600 / 60;
        int secs = seconds % 2678400 % 604800 % 86400 % 3600 % 60;

        if (months > 0)
            return months + " Months " + weeks + " Weeks " + days + " Days\n " + hours + "H " + mins + "m " + secs + "s";
        if (weeks > 0)
            return weeks + " Weeks " + days + " Days " + hours + " H\n " + mins + "m " + secs + "s";
        if (days > 0)
            return days + " Days " + hours + "H " + mins + "m " + secs + "s";
        if (hours > 0)
            return hours + "H " + mins + "m " + secs + "s";
        if (mins > 0)
            return mins + "m " + secs + "s";
        return secs + "s";
    }
}

