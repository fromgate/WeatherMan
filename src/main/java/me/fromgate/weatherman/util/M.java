package me.fromgate.weatherman.util;

import java.text.DecimalFormat;
import java.util.*;

public enum M {

    //Default (lang) messages
    LNG_LOAD_FAIL("Failed to load languages from file. Default message used"),
    LNG_SAVE_FAIL("Failed to save lang file"),
    LNG_PRINT_FAIL("Failed to print message %1%. Sender object is null."),
    LNG_CONFIG("[MESSAGES] Messages: %1% Language: %2% Save translate file: %1% Debug mode: %3%"),
    WORD_UNKNOWN("Unknown"),
    WRONG_PERMISSION("You have not enough permissions to execute this command"),
    PERMISSION_FAIL("You have not enough permissions to execute this command", 'c'),
    PLAYER_COMMAD_ONLY("You can use this command in-game only!", 'c'),
    CMD_REGISTERED("Command registered: %1%"),
    CMD_FAILED("Failed to execute command. Type %1% to get help!"),
    HLP_TITLE("%1% | Help"),

    MSG_OUTDATED("%1% is outdated!"),
    MSG_PLEASEDOWNLOAD("Please download new version (%1%) from "),
    HLP_HELP("Help"),
    HLP_THISHELP("%1% - this help"),
    HLP_EXECCMD("%1% - execute command"),
    HLP_TYPECMD("Type %1% - to get additional help"),
    HLP_TYPECMDPAGE("Type %1% - to see another page of this help"),

    HLP_CMDPARAM_COMMAND("command"),
    HLP_CMDPARAM_PAGE("page"),
    HLP_CMDPARAM_PARAMETER("parameter"),
    CMD_PLAY("%1% - play effect"),

    CMD_UNKNOWN("Unknown command: %1%"),
    CMD_CMDPERMERR("Something wrong (check command, permissions)"),
    ENABLED("enabled"),
    DISABLED("disabled"),
    LST_TITLE("String list:"),
    LST_FOOTER("Page: [%1% / %2%]"),
    LST_LISTISEMPTY("List is empty"),
    CFGMSG_GENERAL_CHECK_UPDATES("Check updates: %1%"),
    CFGMSG_GENERAL_LANGUAGE("Language: %1%"),
    CFGMSG_GENERAL_LANGUAGE_SAVE("Save translation file: %1%"),
    CMD_BIOME("%1% - set the current biome value"),
    CMD_GIVE("%1% - give wand item"),
    CMD_REPOPULATE("%1% - repopulate area. Warning! Could break your buildings!"),
    CMD_CFG("%1% - configure plugin"),
    CMD_CHECK("%1% - check biome in player location"),
    CMD_FILL("%1% - replace current biome with new one"),
    CMD_REPLACE("%1% - replace <biome1> with <biome2> inside selected WorldEdit region"),
    CMD_HELP("%1% - display help"),
    CMD_LIST("%1% - list all biome types"),
    CMD_RADIUS("%1% - set the current radius value"),
    CMD_SET("%1% - set the biome around the player or at defined WorldGuard region (if radius or region name is skipped it will change biome at selected WorldEdit region)"),
    CMD_WAND("%1% - toggles wand mode"),
    CMD_WALKINFO("%1% - toggles walk-info mode"),
    MSG_WRONG("Something wrong (check command, permissions)"),
    MSG_BIOMELIST("Biome list: %1%"),
    MSG_BIOMELOC("Biome in your location is set to %1%"),
    MSG_BIOMELOC2("Biome in your location is set to %1%. Original biome: %2%"),
    MSG_MOVETOBIOME("You enter biome %1%"),
    MSG_MOVETOBIOME2("You enter biome %1%. Original biome: %2%"),
    MSG_CURBIOME("Current biome is set to: %1%"),
    MSG_DEFRADIUS("Current radius is set to: %1% (default)"),
    MSG_WANDMODE("Wand mode is %1%"),
    MSG_BIOMERADIUS("Biome: %1% Radius: %2%"),
    MSG_CONFIG("Configuration"),
    CFG_PLGCONFIG("Plugin configuration:"),
    CFG_WANDITEM("Wand item id: %1%"),
    CFG_MELT("Melt snow: %1% melt ice: %2%"),
    CFG_MOBSPAWN("PigZombies and Ghasts spawning in Normal world: %1%"),
    CFG_DEFBIOME("Default Biome: %1%"),
    CFG_DEFRADIUS("Default radius: %1%"),
    CFG_MAXRADIUS("Maximum radius:"),
    CFG_MAXRCMD("Command - %1%"),
    CFG_MAXRWAND("Wand - %1%"),
    CFG_MAXRSIGN("Sign - %1%"),
    CFG_PLAYER("Current player configuration:"),
    CFG_WANDBIOMERADIUS("Wand mode: %1% Biome: %2% Radius: %3%"),
    HLP_COMMANDS("Commands: %1%"),
    MSG_BIOMESET("Biome in selected region was set to %1%"),
    MSG_BIOMESTATS("Execution time: %1% sec. Blocks: %2% (Chunks: %3%)"),
    MSG_BIOMEREPLACE("Biome %1% in selected region was changed to %2%"),
    MSG_BIOMEREPLACEREGION("Biome %1% in region %3% was changed to %2%"),
    MSG_BIOMEREPLACEAREA("Biome %1% at area [%3%] (%4%, %5%)x(%6%, %7%) was changed to %2%"),
    MSG_WRONGBIOME("Wrong biome name %1% Type /wm list to show all possible biome types"),
    MSG_SELECTREGION("Select a region with WorldEdit first", 'c'),
    MSG_NEEDWORLDEDIT("WorldEdit was not found", 'c'),
    MSG_BIOMEUNKNOWN("Unknown biome: %1%"),
    MSG_WORLDUNKNOWN("Unknown world: %1%"),
    MSG_CURRADIUS("Current radius is set to: %1%"),
    MSG_WRONGRADIUS("Wrong radius: %1%"),
    MSG_WRONGAREA("Wrong area coordinates: %1%"),
    MSG_CMDUNKNOWN("Unknown command/parameter %1%"),
    MSG_BIOMEAROUND("Biome around you was changed to %1%"),
    MSG_BIOMEAROUNDLOC("Biome around location [%2%] (%3%, %4%) was changed to %1%"),
    MSG_BIOMEREGION("Biome at region %1% you was changed to %2%"),
    MSG_BIOMEAREA("Biome at area [%2%] (%3%, %4%)x(%5%, %6%) was changed to %1%"),
    MSG_WANDSET("Wand id is set to: %1%"),
    MSG_WRONGWAND("Wrong wand item id: %1%"),
    MSG_SMOKECHANCE("Smoke chance is set to: %1%"),
    MSG_WRONGSMCH("Wrong smoke chance value: %1%"),
    MSG_DEFRADIUSWRONG("Wrong default radius value: %1%"),
    MSG_DEFRADIUSSET("Default radius is set to: %1%"),
    MSG_DEFBIOMESET("Default biome is set to: %1%"),
    MSG_DEFBIOMEWRONG("Wrong default biome value: %1%"),
    MSG_MOBSPAWN("Spawning nether mobs in normal worlds: %1%"),
    MSG_SMOKE("Smoke effect is %1%"),
    MSG_MELTSNOW("Melting snow on biome change is %1%"),
    MSG_MELTICE("Melting ice on biome change is %1%"),
    MSG_MAXRADCMD("Maximum radius (command) is set to: %1%"),
    MSG_MAXRADWRONG("Wrong maximum radius value: %1%"),
    MSG_MAXRADWAND("Maximum radius (wand) is set to: %1%"),
    MSG_MAXRADSIGN("Maximum radius (sign) is set to: %1%"),
    MSG_WANDMODEDISABLED("WeatherMan wand mode was %1%. Type %2% to enable it again"),
    CFG_NOSNOW("Snow-forming is disabled at biomes: %1%"),
    CFG_NOICE("Ice-forming is disabled at biomes: %1%"),
    CFG_NOSNOW_EMPTY("Snow-forming is enabled at all cold biomes"),
    CFG_NOICE_EMPTY("Ice-forming is enabled at all cold biomes"),
    WG_UNKNOWNREGION("Unknown WorldGuard region: %1%", 'c', '4'),
    WG_NOTFOUND("WorldGuard plugin is not found (Is it installed?)", 'c'),
    MSG_WALKINFO("Walk-info mode"),
    WTH_UNKNOWNPLAYER("Cannot change the personal wth. Player %1% is unknown."),
    WTH_PLAYERWEATHER("Weather state for player %1% was set to %2%"),
    WTH_PLAYERWEATHERREMOVED("Personal wth setting for player %1% was removed!"),
    WTH_BIOMEWEATHER("Weather state for biome %1% was set to %2%"),
    WTH_BIOMEWEATHERREMOVED("Weather settings for biome %1% was removed!"),
    WTH_REGIONWEATHER("Weather state for region %1% was set to %2%"),
    WTH_REGIONWEATHERREMOVED("Weather settings for region %1% was removed!"),
    WTH_WORLDWEATHER("Weather state for world %1% was set to %2%"),
    WTH_WORLDWEATHERREMOVED("Weather settings for world %1% was removed!"),
    WTH_PLAYERLIST("Player wth settings:"),
    WTH_PLAYERLISTEMPTY("Personal wth list is empty", 'c'),
    WTH_REGIONLIST("Region wth settings:"),
    WTH_REGIONLISTEMPTY("Region wth list is empty", 'c'),
    WTH_BIOMELIST("Biome wth settings:"),
    WTH_BIOMELISTEMPTY("Biome wth list is empty", 'c'),
    WTH_WORLDLIST("World wth settings:"),
    WTH_WORLDLISTEMPTY("World wth list is empty", 'c'),
    WTH_UNKNOWNWEATHER("Unknown wth %1% (must be \"rain\" or \"clear\")", 'c', '4'),
    WTH_UNKNOWNBIOME("Cannot change the biome wth. Biome %1% is unknown.", 'c', '4'),
    WTH_UNKNOWNREGION("Cannot change the region wth. Region %1% is unknown.", 'c', '4'),
    WTH_UNKNOWNWORLD("Cannot change the world wth. World %1% is unknown.", 'c', '4'),
    WTH_BIOME("%1% - set wth state for defined biome"),
    WTH_WORLD("%1% - set wth state for defined world"),
    WTH_REGION("%1% - set wth state for defined WorldGuard region"),
    WTH_PLAYER("%1% - set wth state for defined player"),
    WTH_ENABLED("Local wth feature: %1%. You need to restart server to take effect."),
    WTH_SORRYDISABLED("Action declined. Type %1% and restart server to enable local wth features."),
    RAIN("rain"),
    CLEAR("clear"),
    MSG_WANDITEMGIVEN("Wand %1% was added to your inventory"),
    MSG_CMDNEEDPLAYER("This command could be executed by player only", 'c'),
    MSG_WRONGLOCATION("Wrong location format", 'c'),
    MSG_QUEUEBIOMEFINISH("Biome changed. Time %1% Chunks: %2% Columns: %3%"),
    MSG_QUEUEPOPULATEFINISH("Area repopulated Time %1% Chunks: %2% Columns: %3%"),
    MINSEC("%1% min. %2% sec."),
    SEC("%1% sec."),
    MSG_WANDCONFIG("Wand: %1% Biome: %2% Radius: %3% Tree: %4%"),
    MSG_WANDLIST("Use command /wm give <wand name>. Availiable wands: %1%"),
    MSG_TREELIST("Known tree types: %1%");


    private static Messenger messenger;

    private static boolean debugMode = false;
    private static String language = "default";
    private static boolean saveLanguage = false;
    private static char c1 = 'a';
    private static char c2 = '2';
    private static String pluginName;


    public static String colorize(String text) {
        return messenger.colorize(text);
    }


    /**
     * This is my favorite debug routine :) I use it everywhere to print out variable values
     *
     * @param s - array of any object that you need to print out.
     *          Example:
     *          Message.BC ("variable 1:",var1,"variable 2:",var2)
     */
    public static void BC(Object... s) {
        if (!debugMode) return;
        if (s.length == 0) return;
        StringBuilder sb = new StringBuilder("&3[").append(pluginName).append("]&f ");
        for (Object str : s)
            sb.append(str.toString()).append(" ");

        messenger.broadcast(colorize(sb.toString().trim()));
    }

    /**
     * Send current message to log files
     *
     * @param s
     * @return — always returns true.
     * Examples:
     * Message.ERROR_MESSAGE.log(variable1); // just print in log
     * return Message.ERROR_MESSAGE.log(variable1); // print in log and return value true
     */
    public boolean log(Object... s) {
        messenger.log(getText(s));
        return true;
    }

    /**
     * Same as log, but will printout nothing if debug mode is disabled
     *
     * @param s
     * @return — always returns true.
     */
    public boolean debug(Object... s) {
        if (debugMode)
            log(messenger.clean(getText(s)));
        return true;
    }

    /**
     * Show a message to player in center of screen (this routine unfinished yet)
     *
     * @param seconds — how much time (in seconds) to show message
     * @param sender  — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip(int seconds, Object sender, Object... s) {
        return messenger.tip(seconds, sender, getText(s));
    }
    /*
    public boolean tip(int seconds, CommandSender sender, Object... s) {
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        final Player player = sender instanceof Player ? (Player) sender : null;
        final String message = getText(s);
        if (player == null) sender.sendMessage(message);
        else for (int i = 0; i < seconds; i++)
            Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
                public void run() {
                    if (player.isOnline()) player.sendTip(message);
                }
            }, 20 * i);
        return true;
    } */

    /**
     * Show a message to player in center of screen
     *
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip(Object sender, Object... s) {
        return messenger.tip(sender, getText(s));
    }

    /**
     * Send message to Player or to ConsoleSender
     *
     * @param sender
     * @param s
     * @return — always returns true.
     */
    public boolean print(Object sender, Object... s) {
        if (sender == null) return M.LNG_PRINT_FAIL.log(this.name());
        return messenger.print(sender, getText(s));
    }

    /**
     * Send message to all players or to players with defined permission
     *
     * @param permission
     * @param s
     * @return — always returns true.
     * <p>
     * Examples:
     * Message.MSG_BROADCAST.broadcast ("pluginname.broadcast"); // send message to all players with permission "pluginname.broadcast"
     * Message.MSG_BROADCAST.broadcast (null); // send message to all players
     */
    public boolean broadcast(String permission, Object... s) {
        return messenger.broadcast(permission, getText(s));
    }


    /**
     * Get formated text.
     *
     * @param keys * Keys - are parameters for message and control-codes.
     *             Parameters will be shown in position in original message according for position.
     *             This keys are used in every method that prints or sends message.
     *             <p>
     *             Example:
     *             <p>
     *             EXAMPLE_MESSAGE ("Message with parameters: %1%, %2% and %3%");
     *             Message.EXAMPLE_MESSAGE.getText("one","two","three"); //will return text "Message with parameters: one, two and three"
     *             <p>
     *             * Color codes
     *             You can use two colors to define color of message, just use character symbol related for color.
     *             <p>
     *             Message.EXAMPLE_MESSAGE.getText("one","two","three",'c','4');  // this message will be red, but word one, two, three - dark red
     *             <p>
     *             * Control codes
     *             Control codes are text parameteres, that will be ignored and don't shown as ordinary parameter
     *             - "SKIPCOLOR" - use this to disable colorizing of parameters
     *             - "NOCOLOR" (or "NOCOLORS") - return uncolored text, clear all colors in text
     *             - "FULLFLOAT" - show full float number, by default it limit by two symbols after point (0.15 instead of 0.1483294829)
     * @return
     */
    public String getText(Object... keys) {
        char[] colors = new char[]{color1 == null ? c1 : color1, color2 == null ? c2 : color2};
        if (keys.length == 0) return colorize("&" + colors[0] + this.message);
        String str = this.message;
        boolean noColors = false;
        boolean skipDefaultColors = false;
        boolean fullFloat = false;
        String prefix = "";
        int count = 1;
        int c = 0;
        DecimalFormat fmt = new DecimalFormat("####0.##");
        for (int i = 0; i < keys.length; i++) {
            String s = messenger.toString(keys[i], fullFloat);//keys[i].toString();
            if (c < 2 && keys[i] instanceof Character) {
                colors[c] = (Character) keys[i];
                c++;
                continue;
            } else if (s.startsWith("prefix:")) {
                prefix = s.replace("prefix:", "");
                continue;
            } else if (s.equals("SKIPCOLOR")) {
                skipDefaultColors = true;
                continue;
            } else if (s.equals("NOCOLORS") || s.equals("NOCOLOR")) {
                noColors = true;
                continue;
            } else if (s.equals("FULLFLOAT")) {
                fullFloat = true;
                continue;
            } else if (keys[i] instanceof Double || keys[i] instanceof Float) {
                if (!fullFloat) s = fmt.format((Double) keys[i]);
            }

            String from = (new StringBuilder("%").append(count).append("%")).toString();
            String to = skipDefaultColors ? s : (new StringBuilder("&").append(colors[1]).append(s).append("&").append(colors[0])).toString();
            str = str.replace(from, to);
            count++;
        }
        str = colorize(prefix.isEmpty() ? "&" + colors[0] + str : prefix + " " + "&" + colors[0] + str);
        if (noColors) str = clean(str);
        return str;
    }

    public static String clean(String str) {
        return messenger.clean(str);
    }

    private void initMessage(String message) {
        this.message = message;
    }

    private String message;
    private Character color1;
    private Character color2;

    M(String msg) {
        message = msg;
        this.color1 = null;
        this.color2 = null;
    }

    M(String msg, char color1, char color2) {
        this.message = msg;
        this.color1 = color1;
        this.color2 = color2;
    }

    M(String msg, char color) {
        this(msg, color, color);
    }

    @Override
    public String toString() {
        return this.getText("NOCOLOR");
    }

    /**
     * Initialize current class, load messages, etc.
     * Call this file in onEnable method after initializing plugin configuration
     */
    public static void init(String pluginName, Messenger mess, String lang, boolean debug, boolean save) {
        messenger = mess;
        language = lang;
        debugMode = debug;
        saveLanguage = save;
        initMessages();
        if (saveLanguage) saveMessages();
        LNG_CONFIG.debug(M.values().length, language, true, debugMode);
    }

    /**
     * Enable debugMode
     *
     * @param debug
     */
    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    public static boolean isDebug() {
        return debugMode;
    }


    private static void initMessages() {
        Map<String, String> lng = messenger.load(language);
        for (M key : M.values()) {
            if (lng.containsKey(key.name().toLowerCase())) {
                key.initMessage(lng.get(key.name().toLowerCase()));
            }
        }
    }

    private static void saveMessages() {
        Map<String, String> messages = new LinkedHashMap<String, String>();
        for (M msg : M.values()) {
            messages.put(msg.name().toLowerCase(), msg.message);
        }
        messenger.save(language, messages);
    }

    /**
     * Send message (formed using join method) to server log if debug mode is enabled
     *
     * @param s
     */
    public static boolean debugMessage(Object... s) {
        if (debugMode) messenger.log(clean(join(s)));
        return true;
    }

    /**
     * Join object array to string (separated by space)
     *
     * @param s
     */
    public static String join(Object... s) {
        StringBuilder sb = new StringBuilder();
        for (Object o : s) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(messenger.toString(o, false));
        }
        return sb.toString();
    }

    public static void printLines(Object sender, Collection<String> lines) {
        for (String l : lines) {
            messenger.print(sender, colorize(l));
        }
    }

    public static void printPage(Object sender, List<String> lines, M title, int pageNum, int linesPerPage) {
        printPage(sender, lines, title, null, pageNum, linesPerPage);
    }

    public static void printPage(Object sender, List<String> lines, M title, M footer, int pageNum, int linesPerPage) {
        if (lines == null || lines.isEmpty()) return;
        List<String> page = new ArrayList<String>();
        if (title != null) page.add(title.message);

        int pageCount = lines.size() / linesPerPage + 1;
        if (pageCount * linesPerPage == lines.size()) pageCount = pageCount - 1;

        int num = pageCount <= pageNum ? pageNum : 1;

        for (int i = linesPerPage * (num - 1); i < Math.min(lines.size(), num * linesPerPage); i++) {
            page.add(lines.get(i));
        }
        if (footer != null) page.add(num, footer.getText(pageCount));
        printLines(sender, page);
    }

    public static boolean logMessage(Object... s) {
        messenger.log(clean(join(s)));
        return true;
    }


    public static M getByName(String name) {
        for (M m : values()) {
            if (m.name().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public static String enDis(boolean value) {
        return value ? M.ENABLED.toString() : M.ENABLED.toString();
    }
}