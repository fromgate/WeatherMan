/*
 *  WeatherMan, Minecraft bukkit plugin
 *  ©2012-2018, fromgate, fromgate@gmail.com
 *  https://www.spigotmc.org/resources/weatherman.43379/
 *
 *  This file is part of WeatherMan.
 *
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package me.fromgate.weatherman.commands;

import me.fromgate.weatherman.util.lang.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;


public abstract class Cmd {
    private String command;
    private String[] subCommands;
    private String permission;
    private boolean allowConsole;
    private M description;
    private String shortDescription;


    public Cmd() {
        if (this.getClass().isAnnotationPresent(CmdDefine.class)) {
            CmdDefine cd = this.getClass().getAnnotation(CmdDefine.class);
            this.command = cd.command();
            this.subCommands = cd.subCommands();
            this.permission = cd.permission();
            this.allowConsole = cd.allowConsole();
            this.description = cd.description();
            this.shortDescription = cd.shortDescription();
        }
    }

    public boolean canExecute(CommandSender sender) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) return this.allowConsole;
        if (this.permission == null || this.permission.isEmpty()) return true;
        return player.hasPermission(this.permission);
    }

    public boolean isValidCommand() {
        return (this.getCommand() != null && !this.getCommand().isEmpty());
    }

    public String getCommand() {
        return this.command;
    }

    public boolean checkParams(String[] params) {
        if (this.subCommands == null || this.subCommands.length == 0) {
            return true;
        }
        if (params.length < this.subCommands.length) {
            return false;
        }
        for (int i = 0; i < this.subCommands.length; i++) {
            if (!params[i].matches("(?i)" + this.subCommands[i])) return false;
        }
        return true;
    }

    public boolean executeCommand(CommandSender sender, String[] params) {
        if (!canExecute(sender)) {
            return false;
        }
        if (!this.checkParams(params)) {
            return false;
        }
        if (this.allowConsole) {
            return execute(sender, params);
        } else {
            return execute((Player) sender, params);
        }
    }

    public boolean execute(Player player, String[] params) {
        Commander.getPlugin().getLogger().info("Command \"" + this.getCommand() + "\" executed but method \"public boolean execute(Player player, String [] params)\" was not overrided");
        return false;
    }

    public boolean execute(CommandSender sender, String[] params) {
        Commander.getPlugin().getLogger().info("Command \"" + this.getCommand() + "\" executed but method \"public boolean execute(CommandSender sender, String [] params)\" was not overrided");
        return false;
    }

    public String getFullDescription() {
        return this.description.getText(this.shortDescription);
    }

    public List<String> getSubCommands() {
        return Arrays.asList(this.subCommands);
    }
}
