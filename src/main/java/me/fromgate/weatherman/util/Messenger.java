package me.fromgate.weatherman.util;

import java.util.Map;

public interface Messenger {

    String colorize(String text);

    boolean broadcast(String colorize);

    boolean log(String text);

    String clean(String text);

    boolean tip(int seconds, Object sender, String text);

    boolean tip(Object sender, String text);

    boolean print(Object sender, String text);

    boolean broadcast(String permission, String text);

    String toString(Object obj, boolean fullFloat);

    Map<String, String> load(String language);

    void save(String langugage, Map<String, String> message);

    boolean isValidSender(Object send);

}
