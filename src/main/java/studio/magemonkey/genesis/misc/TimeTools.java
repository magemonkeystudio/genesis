package studio.magemonkey.genesis.misc;

import studio.magemonkey.genesis.managers.ClassManager;

public class TimeTools {


    public final static int SECOND = 1;
    public final static int MINUTE = SECOND * 60;
    public final static int HOUR   = MINUTE * 60;
    public final static int DAY    = HOUR * 24;
    public final static int WEEK   = DAY * 7;

    /**
     * Transform a long into time
     *
     * @param timeInSeconds the time to transform
     * @return transformed string
     */
    public static String transform(long timeInSeconds) {
        if (timeInSeconds > WEEK) {
            return ClassManager.manager.getMessageHandler()
                    .get("Time.Weeks")
                    .replace("%time%", String.valueOf(timeInSeconds / WEEK));
        }
        if (timeInSeconds > DAY) {
            return ClassManager.manager.getMessageHandler()
                    .get("Time.Days")
                    .replace("%time%", String.valueOf(timeInSeconds / DAY));
        }
        if (timeInSeconds > HOUR) {
            return ClassManager.manager.getMessageHandler()
                    .get("Time.Hours")
                    .replace("%time%", String.valueOf(timeInSeconds / HOUR));
        }
        if (timeInSeconds > MINUTE) {
            return ClassManager.manager.getMessageHandler()
                    .get("Time.Minutes")
                    .replace("%time%", String.valueOf(timeInSeconds / MINUTE));
        }
        return ClassManager.manager.getMessageHandler()
                .get("Time.Seconds")
                .replace("%time%", String.valueOf(timeInSeconds));
    }

}
