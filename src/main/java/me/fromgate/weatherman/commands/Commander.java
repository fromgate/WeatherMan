/*
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2016, fromgate, fromgate@gmail.com
 *  https://dev.bukkit.org/projects/weatherman
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

import me.fromgate.weatherman.commands.self.MyTime;
import me.fromgate.weatherman.commands.self.MyWeather;
import me.fromgate.weatherman.commands.wm.*;
import me.fromgate.weatherman.commands.wmt.WtmBiome;
import me.fromgate.weatherman.commands.wmt.WtmPlayer;
import me.fromgate.weatherman.commands.wmt.WtmRegion;
import me.fromgate.weatherman.commands.wmt.WtmWorld;
import me.fromgate.weatherman.commands.wth.WthBiome;
import me.fromgate.weatherman.commands.wth.WthPlayer;
import me.fromgate.weatherman.commands.wth.WthRegion;
import me.fromgate.weatherman.commands.wth.WthWorld;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commander implements CommandExecutor {
    private static List<Cmd> commands;
    private static JavaPlugin plugin;
    private static Commander commander;
    private static Cmd helpCommand;

    public static void init(JavaPlugin plg) {
        commands = new ArrayList<>();
        plugin = plg;
        commander = new Commander();
        helpCommand = null;
        addNewCommands(WmHelp.class, WmSet.class, WmReplace.class, WmPopulate.class,
                WmdWand.class, WmGive.class, WmCheck.class, WmInfo.class, WmList.class,
                WthPlayer.class, WthRegion.class, WthBiome.class, WthWorld.class,
                WtmPlayer.class, WtmRegion.class, WtmBiome.class, WtmWorld.class,
                MyWeather.class, MyTime.class);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }


    public static void addNewCommands(Class<? extends Cmd>... cmdClasses) {
        for (Class<? extends Cmd> cmdClass : cmdClasses) {
            try {
                addNewCommand(cmdClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean addNewCommand(Cmd cmd) {
        if (cmd.getCommand() == null) return false;
        if (cmd.getCommand().isEmpty()) return false;
        plugin.getCommand(cmd.getCommand()).setExecutor(commander);
        commands.add(cmd);
        if (cmd instanceof WmHelp) {
            helpCommand = cmd;
        }
        return true;
    }

    public static boolean isPluginYml(String cmdStr) {
        return plugin.getDescription().getCommands().containsKey(cmdStr);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmdLabel, String[] args) {
        for (Cmd cmd : commands) {
            if (!cmd.getCommand().equalsIgnoreCase(command.getLabel())) continue;
            if (cmd.executeCommand(sender, args)) return true;
        }
        if (args.length == 0) {
            helpCommand.executeCommand(sender, new String[]{"help"});
            return true;
        }
        return false;
    }

    public static void printHelp(CommandSender sender, int page) {
        List<String> helpList = new ArrayList<>();
        for (Cmd cmd : commands) {
            if (cmd.canExecute(sender)) {
                helpList.add(cmd.getFullDescription());
            }
        }
        int pageHeight = (sender instanceof Player) ? 5 : 1000;
        M.printPage(sender, helpList, M.HLP_TITLE, M.LST_FOOTER, page, pageHeight);
    }

    public static ChatPage paginate(List<String> unpaginatedStrings, int pageNumber, int lineLength, int pageHeight) {
        List<String> lines = new ArrayList<>();
        for (String str : unpaginatedStrings) {
            lines.addAll(Arrays.asList(ChatPaginator.wordWrap(str, lineLength)));
        }
        int totalPages = lines.size() / pageHeight + (lines.size() % pageHeight == 0 ? 0 : 1);
        int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
        int from = (actualPageNumber - 1) * pageHeight;
        int to = from + pageHeight <= lines.size() ? from + pageHeight : lines.size();
        String[] selectedLines = Arrays.copyOfRange(lines.toArray(new String[lines.size()]), from, to);
        return new ChatPage(selectedLines, actualPageNumber, totalPages);
    }

}
