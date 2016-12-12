package me.fromgate.weatherman.commands;

import me.fromgate.weatherman.util.M;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdDefine {
    public String command();

    public String[] subCommands();

    public String permission();

    public boolean allowConsole() default false;

    public M description();

    public String shortDescription();
}

