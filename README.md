# WeatherMan
*A Minecraft (bukkit) plugin*

WeatherMan allows you to change biomes and repopulate areas ingame with commands,
magic-wand and a sign, controlled by redstone power.
WorldEdit selections and WorldGuard regions supported.

[**Download**](https://dev.bukkit.org/bukkit-plugins/weatherman/)

[**Latest dev build**](https://circleci.com/gh/fromgate/WeatherMan) ![Snapshot](https://circleci.com/gh/fromgate/WeatherMan.svg?style=shield&circle-token=2bb628276a494b5ac51ec0a0ae4deda896b6f3e3)


## Video

[![CameraObscura Video](http://img.youtube.com/vi/xt57rn_j8KA/mqdefault.jpg)](https://youtu.be/xt57rn_j8KA)

See more videos [here](https://youtu.be/b7SiAiM5V9s?list=PLlAz3KfoBuy_8P3VXNMQ7LT5pgY8ze-S_).

## Features
* Change biomes (you can use: commands, wands, signs, WorldEdit selections and WorldGuard regions)
* Repopulate area according to biome that was previously changed
* Control weather locally: for player, for region, for biome and for whole world.
* Grow trees or cut them off using special wands
* Control biome changing (toggle two biomes) with sign controlled by redstone power.
* Check biome in a your location or inform you about biome changing while you traveling
* Preventing snow-forming and ice-forming in defined biomes.

## Why do I want it?
If you need to change biomes, grow new forest, or going to allow your players to do it - you must install WeatherMan :) If you need to "curse" any player with unstoppable rain you can find this plugin useful too.

## How Do I Install It?
Just copy it in your server `/plugins/` folder.

## How to use it?
* Install
* Use command `/wm set biome:<biome> radius:<radius>` to change biome around you
* Use command `/wm set biome:<biome> to change biome` at area selected using WorldEdit
* Use command `/wm set biome:<biome> region:<region name>` to change biome at WorldGuard region defined by name
* Use command `/wm replace biome:<biome> fill:true` to change biome in your location to new one
* Use command `/wm give [biome | woodcutter | depopulator | forester]` to obtain all or specified wand.
* Use command `/wm wand` to enable wand mode (define biome and radius with commands /wm wand biome:<biome> radius:<radius> tree:<tree type> Right-click with magic wand and you'll shoot the snowball that will change the biome around the hit-point, remove or grow trees.
* Create sign and type `[biome]` in second line, radius value in third, and biome names in first and forth lines. You can set word "replace" in third line if you need to replace the full biome. Now you can use redstone power to toggle between two biomes.

## Commands
### General Commands:
`/wm help` - display help and command list  
`/wm check` - check biome in player location  
`/wm info` - toggle walk-info mode (inform player when he moves from one biome to another)  
`/wm list [tree | biome name mask]` - list avaliable biome types (or tree types)  
`/wm wand [biome:<biome> radius:<radius> tree:<tree>` - toggles wand mode or configure wand parameters  
`/wm give <biome | woodcutter | depopulator | forester>` - give defined wand (brush)

### Change Biome Commands
`/wm set biome:<biome|original> radius:<radius>` - set the biome around the player (size defined by radius)  
`/wm set biome:<biome|original> loc:<world,x,z>` - set the biome around location (size defined by radius)  
`/wm set biome:<biome|original> region:<region name>` - set the biome at WorldGuard region  
`/wm set biome:<biome|original>` - set the biome in a WorldEdit selection  
`/wm set biome:<biome|original> loc:<world,x,z> loc2:<world,x,z>` - set the biome at area defined by two locations  

### Replace Biome Commands
`/wm replace biome:<biome|original> source:<biome> radius:<radius>` - replace <source> biome with <biome> around the player (size defined by radius)  
`/wm replace biome:<biome|original> source:<biome> loc:<world,x,z>` - replace <source> biome with <biome> around location (size defined by radius)  
`/wm replace biome:<biome|original> source:<biome> region:<region name>` - replace <source> biome with <biome> at WorldGuard region  
`/wm replace biome:<biome|original> source:<biome>` - replace <source> biome with <biome> the biome in a WorldEdit selection  
`/wm replace biome:<biome|original> source:<biome> loc:<world,x,z> loc2:<world,x,z>` - replace <source> biome with <biome> at area defined by two locations  
`/wm replace biome:<biome|original> fill:true` - replace current biome in player location with <biome>  
`/wm replace biome:<biome|original> fill:true loc:<world,x,z>` - replace current biome in location <world,x,z> with <biome>

### Repopulate Area Commands
`/wm populate radius:<radius>` - repopulates area around the player (size defined by radius)  
`/wm populate loc:<world,x,z>` - repopulates area around location (size defined by radius)  
`/wm populate region:<region name>` - repopulates area inside WorldGuard region  
`/wm populate` - repopulates area inside WorldEdit selection  
`/wm populate loc:<world,x,z> loc2:<world,x,z>` - repopulates area defined by two locations


### Weather Commands
`/wth player <player> <rain|clear|remove>` - set personal player weather to rain, clear or remove settings  
`/wth region <region> <rain|clear|remove>` - set weather in region to rain, clear or remove settings  
`/wth biome <biome> <rain|clear|remove>` - set weather in biome to rain, clear or remove settings  
`/wth world <world> <rain|clear|remove>` - set weather in biome to rain, clear or remove settings  
`/wth <player|region|biome|world>` - list the players, regions, biomes, worlds where weather states are changed  

### Local Time Commands
`/wtm player [<player> <HH:MM|day|night|remove>]` — configure player personal time  
`/wtm region [<region> <HH:MM|day|night|remove>]` — configure local region time  
`/wtm biome [<biome> <HH:MM|day|night|remove>]` — configure local biome time  
`/wtm world [<world> <HH:MM|day|night|remove>]` — configure local world time

## How to revert back to original biomes
If you need to set back an original biomes at defined area, you can use `original` biome type with commands /wm set, /wm replace, /wm biome (and use wand to change biomes). Command /wm replace source:<source biome> biome:<target biome> supporting "original" only as biome parameter (`<target biome>`).

## Permissions
`weatherman.basic` - allows to use commands `/wm help`, `/wm check`, `/wm list`  
`weatherman.cmdbiome` - allows to use command `/wm set` and `/wm fill`
`weatherman.sign` - allows to create `[biome]` signs
`weatherman.wandbiome` - allows to use wand and commands `/wm wand`, `/wm radius`, `/wm biome`
`weatherman.config` - allows to setup plugin with command `/wm cfg`
`weatherman.weather` - allows to use local weather command `/wth` 
`weatherman.time` - allows to use local time command `/wtm` 

## Update checker and bStats
WeatherMan includes two features that use your server internet connection.
First one is bStats, that used to collect information about the plugin (versions of plugin, of Java.. etc.) and second is update checker, checks new releases of plugin after WeatherMan startup and every half hour. This feature is using API provided by dev.bukkit.org. If you don't like this features you can easy disable it. To disable update checker you need to set parameter "version-check" to "false" in config.yml. Obtain more information about bStats and learn how to switch off it, you can read [here](https://bstats.org/getting-started).