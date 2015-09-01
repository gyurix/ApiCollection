[img]http://www.kephost.com/images/2014/11/13/Api-Collection.png[/img]

[size=14pt]This plugin is a collection of some really important missing APIs to Rainbow.[/size]

[center][b][size=18pt]Who are you?[/size][/b][/center]
[b][size=14pt]Server Admin?[/size][/b][spoiler]
[b]Replace the old TabAPI, BarAPI and ConfigFile to this plugin, but before replacing make sure you did the following things:
[/b]
- Stop the server
- Rename [b]_ConfigFile[/b] folder to [b]_ApiCollection[/b]
- The file [b]config.yml[/b] to [b]players.yml[/b]
- Move [b]config.yml[/b] file from [b]BarAPI[/b] folder to [b]_ApiCollection[/b] folder
- Rename it to [b]BarAPI.yml[/b]
- Remove the old plugins (ConfigFile, TabAPI and BarAPI) from plugins_mod folder
- Add this plugin to the [b]plugins_mod [/b]folder
- Start the server
[b]If you didn't have these plugins before, then you can just put the jar file to the plugins_mod folder.

I hope that I detailly wrote this migration thing, so nobody will report for not working for wrongly executed migration method or for no migration done.[/b][/spoiler]
[b][size=14pt]Plugin developer?[/size][/b][spoiler]
You [b]must[/b] use this plugin for your plugins configuration, language, chat, tab, bar and permission management. If you won't use it, I will be really sad, so please use it.

[b]Don't know how to use it?[/b]
See the [url=gyurix.freeiz.com]JavaDoc[/url] for the list of all the methods. If you don't understand something, please ask it in the [url=http://www.project-rainbow.org/site/plugin-releases/api-collection-1-0-0/new/#new]forum[/url]![/spoiler]

[center][b][size=18pt]Built-in APIs[/size][/b][/center]

[center][b][size=16pt]BarAPI[/size][/b][/center][spoiler]
[size=12pt][b]What is it?[/b][/size][spoiler]
This API is for managing BossBars. BossBars are the bars, which you can see, when you are fighting Ender Dragons or Withers. This API spawns fake withers with fake health and custom name, which can be used for displaying any loading bar with custom colored message.[/spoiler]

[b][size=12pt]Client side limitations:[/size][/b][spoiler]
- The reaction time for enabling or disabling bar can took more seconds
- You can only see the bar, if you can see invisible fake withers, so you can only see it, if you are on the surface[/spoiler]

[size=12pt][b]Testing commands:[/b][/size][spoiler]
[b]Permission for all commands:[/b] barapi.test
[b]Commands for your bar:[/b]
/bar - enable/disable the bar
/barm <message> - change static barmessage
/bartm <interval in milliseconds> <message> - add temporary barmessage
/barh <health> - change static barhealth (1-300)
/barth <interval in milliseconds> <health> - change temporary barhealth

[b]Commands for other players bar:[/b]
/barp <player> - enable/disable the bar
/barpm <player> <message> - change static barmessage
/barptm <player> <interval in milliseconds> <message> - add temporary barmessage
/barph <player> <health> - change static barhealth (1-300)
/barpth <player> <interval in milliseconds> <health> - change temporary barhealth

[b]Commands for every bar:[/b]
/bara - enable/disable the bar
/baram <message> - change static barmessage
/baratm <interval in milliseconds> <message> - add temporary barmessage
/barah <health> - change static barhealth (1-300)
/barath <interval in milliseconds> <health> - change temporary barhealth
[/spoiler]

[b][size=12pt]Configuration[/size][/b]
[spoiler]You can configure in the BarAPI.yml file the exact fake-wither coordinates relative to player, and the updating intervall for handling player movement/teleport.

If you found a better way for placing these withers, please write it me to a comment.[/spoiler]

[size=12pt][b]Changelog:[/b][/size]
[spoiler][b]1.0.0[/b]
- Fixed some no coloring bugs
- Renamed configuration to BarAPI.yml[/spoiler]
[/spoiler]
[center][b][size=16pt]TabAPI[/size][/b][/center][spoiler]
[size=12pt][b]What is it?[/b][/size]
This API is for managing fields in player list (<Tab> button for players). 

[b][size=12pt]Features and client size limitations:[/size][/b][spoiler]
- No character limit for player slots
- No server side tab modification (that's the reality, it only uses packets)
[b]- Real players skins for player slots[/b]
- Resizeable tab support
- Same player names support
- Row and collumn id-s support
- Unbreakable order support
- Back to vanilla tab function using /vtab command
- Ping modification using simple enums
- Packet sending optimalization (only sends the neccessary packets)[/spoiler]


[size=12pt][b]Testing commands:[/b][/size][spoiler]
[b]Permission for all commands:[/b] tabapi.test
[b]/chname <x> <y> <newname>[/b]
Change player name at the given coords, support colorcodes.
[b]/chskin <x> <y> <newskinname>[/b]
Change player skin at the given coords, must be case sensitively valid playername.
[b]/loadskin [delay in milliseconds][/b]
Load all the player skins, if you don't see them for some reason. You need to give the loading time, usually 100 millisecond should work. If not, try it with bigger values.
[b]/chping <x> <y> <newping(0-5)>[/b]
Change player ping at the given coords, use only numbers from 0 to 5.
[b]/chtr <x> <y> <true/false>[/b]
Change player name translucentation, be carefull, activating this forces the player name moving to the end of the player list, so my suggestion is, that use it only for the last some players.
[b]/fill <minx> <miny> <maxx> <maxy> <newtext>[/b]
Fill the given area with the given text
[b]/size <newmaxx> <newmaxy>[/b]
Resize the current tab. Be carefull minecraft generates new collumn after 20 players, that means, you can't use sizes i.e. 2x5.
[b]/dftab[/b]
Set your tab to default tab, which is vanilla tab for default.
[b]/vtab[/b]
Set your tab to vanilla player list
[b]/tab2[/b]
Set your tab to the second tab
[/spoiler]

[b][size=12pt]Configuration[/size][/b]
[spoiler]In configuration you can disable it and you can set all the magic timing in it:
[b]removetime[/b] means the delay for sending real players remove packet after spawn player packet.
[b]firstremovetime[/b] means the delay for sending remove packet when a player joins to server
[b]jointime[/b] means the delay for handling player join after Player join event is called.
All the times are in milliseconds.[/spoiler]

[size=12pt][b]Changelog:[/b][/size]
[spoiler][b]1.0.0[/b]
- Improved first and tab switching packet sending
- Fixed bugs, when you switched from VanillaTab to normal Tab
- Set default tab to vanilla tab.
[b]1.0.1 and 1.0.2[/b]
- Fixed newly joined players aren't visible, for players, that has not vanilla tab.
[b]1.0.5[/b]
- Added configuration for enabling/disabling it
[b]2.0.0[/b]
- Fixed tons of bugs
- Added skin management support
- Rewrited packet management, so now there is no limit for playernames
[/spoiler]

[/spoiler]
[center][b][size=16pt]ConfigFile[/size][/b][/center][spoiler]
[size=12pt][b]What is it?[/b][/size]
This API is for managing configuration and language file files on your server.

[size=12pt][b]Changelog[/b][/size]
[b]1.0.0[/b]
- rewrited language management command
[b]1.0.3[/b]
- added German translations, thank to CloudeLecaw
[b]1.0.4[/b]
- fixed help menu broke, caused by returning null command name
[b]2.0.0[/b]
- rewrited configuration file data storage core, it were 2 lists, now it is a tree map
- it means, now it's speed can be 10-20x faster, then before, thanks to tree maps binary search
[b]2.0.1[/b]
- fixed disabling it cause language file not working

[size=12pt][b]Commands and permissions[/b][/size][spoiler]
[b]/lang [newlanguage] [player][/b]
You can use word [b]CONSOLE[/b] for the player parameter, if you want to change the console language from the game.
[b]kf.langchange[/b]
For listing the languages and setting your language
[b]kf.langchange.player[/b]
For setting another players language
[b]kf.langchange.console[/b]
For setting console language[/spoiler]
[/spoiler]

[center][b][size=16pt]PermissionApi[/size][/b][/center][spoiler]
[size=12pt][b]What is it?[/b][/size]
This is not only an API it's a complete permission management system.

[size=12pt][b]Changelog[/b][/size]
[b]2.0.0[/b]
- release
[b]2.0.2[/b]
- fixed its enabled/disabled state (now if you disable it, then you only disable it's permission handling, but the data management and commands will be still accessible)
- Added toggle subcommand for PermAPI command for easy ingame enabling/disabling it's permission management
- Set default debug mode to disabled

[size=12pt][b]Commands and permissions[/b][/size][spoiler]
[b]/hasperm[/b]
Lists all the registred players

[b]/hasperm playername|g:groupname[/b]
Lists all the permissions for the given player or group.

[b]/hasperm playername|g:groupname permission[/b]
Checks the given permission for the given player/group.

[b]/perm[/b]
Shows the helpline for command perm

[b]/perm playername|g:groupname[/b]
Creates or removes the given player or group.

[b]/perm playername|g:groupname perm [parameters][/b]
Adds/removes the given permission from given player/group. Number of parameters will extend in the next versions, now the only parameter is [b]expire:[/b]permission-expiration-utc-time.

[b]/group[/b]
Lists all available groups.

[b]/group playername|g:groupname[/b]
Lists the groups of one player or subgroups for the given group.

[b]/group playername|g:groupname groupname[/b]
Adds/removes the given group for the given player or group.

[b]/group playername|g:groupname -groupname[/b]
Sets/removes the given group for the given player or group. Setting a group means removing all the previous groups, and set only the given one.

[b]/data playername|g:groupname[/b]
Shows all the defined data for the given player/group

[b]/data playername|g:groupname adress[/b]
Shows all the defined data for the given player/group, which is under the given adress

[b]/data playername|g:groupname adress newvalue[/b]
Sets new value for the given adress.
[b]Special newvalue values:[/b]
[b]
[/b] no value
[b]
[/b] remove the given adress and all its subadress.

[b]/permapi [debug|reload|save][/b]
If you don't use the optional [b][debug|reload|save][/b] argument, then you got some information about the permissionApi (creator, translator, version e.t.c).[/spoiler]

[b]Permission for every command:[/b]
permapi.command.<commandname>

[size=12pt][b]Configuration[/b][/size][spoiler]
[b]players.yml file:[/b]
You need to remove the old file!
format:
playernamewithsmallletter:
  permissions: perm1
perm2
  groups: group1
group2
  data:
     lang: hu
     asd:
        subadress: asd
Group file uses the subgroups keyword instead of the groups keyword.

[b]Permission format:[/b]
[code][b][!][-]permission[*] [parameters][/b][/code]
[b]![/b] means regex based permission.
[b]-[/b] means negated permission.
[b]*[/b] means the permission starts with the text which is before *.
[b]Parameters:[/b]
[b]expire:utc-time[/b]
The expiration of the permission.[/spoiler]
[/spoiler]

[center][b][size=16pt]ChatAPI[/size][/b][/center]
Comming soon...
[b]ChatAPI.msg(String prefix,String suffix,MC_Player plr,String adress,String... replacements)[/b] and 
[b]ChatAPI.msg(MC_Player plr,String adress,String... replacements)[/b] method is already done, so you can use it, for writing out something from LanguageFile.
Custom Json parser is already done :D

[center][b][size=16pt]InventoryAPI[/size][/b][/center]
Planned...

[center][b][size=16pt]Links[/size][/b][/center]
[center][b][size=14pt]As you can read it, this is only a development build for the Api-Collection, but you need to update to it, if you want to use the latest ProtectionCore version. In this version, I added some parts of new incoming ChatAPI, but currently you can't use this feature, because it's not yet done. You don't need to worry about this plugin stage, it's safe to use, because I disabled all the not-done ChatAPI event calls, and also the ChatAPI initialization :D So you won't see any change in it.[/size][/b][/center]

[center][b][size=14pt][url=gyurix.freeiz.com]JavaDoc[/url][/size][/b][/center]
[center][b][size=14pt][url=http://www.project-rainbow.org/site/plugin-releases/api-collection-1-0-0/new/#new]Forum[/url][/size][/b][/center]