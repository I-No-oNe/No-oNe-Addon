# No oNe Addon

A powerful and feature-rich addon for [ClickCrystals](https://github.com/ClickCrystals-Development/ClickCrystals), made
to enhance your
Minecraft PvP and utility experience.

---

## 🧩 Features

This addon includes a collection of **modules** and **commands** designed to give you more control, customization while
playing.

---

## 📦 Modules

| Name                  | Description                                                                    |
|-----------------------|--------------------------------------------------------------------------------|
| GUI Features Disabler | Strips the popups ClickCrystals shows over its own menu                          |
| Infinite Chat         | Lifts the 256 character limit on chat messages                                  |
| Cape Disabler         | Shows vanilla capes instead of ClickCrystals ones, and drops the staff greetings |
| Middle Click Ping     | Middle-click a player to read their ping out in chat                            |
| Loot Keeper           | Throws out your hotbar on a key, keeping only the items worth keeping            |
| No Potion HUD         | Hides the potion effect icons in the corner of the screen                        |
| Hit Color             | Recolors the flash an entity shows when it takes a hit                          |
| DM Spammer            | Repeats a private message to a list of players on a timer                        |
| Mod Menu Disabler     | Hides chosen mods from Mod Menu, applied on the next launch                      |
| Upside Down           | Flips the entities around you upside down                                       |
| No Misplace           | Blocks the glowstone and anchor placements that waste an anchor                  |
| Anti Off Hand         | Blocks the off-hand swap key while you are holding items you cannot afford to move |
| Ghost Interactions    | Keeps containers open server side by never sending the close packet              |
| Safe Walk             | Holds you back at block edges the way sneaking does                              |
| Name Changer          | Replaces your name with another one everywhere the client draws it                |

---

## 💻 Commands

| Name           | Description                                                      |
|----------------|------------------------------------------------------------------|
| `/clean`       | Clears specific lines from chat or log files                     |
| `/free-ram`    | Attempts to free up system memory                                |
| `/quit`        | Instantly quits the game or disconnects from server              |
| `/hwid`        | Displays the user's HWID (Hardware ID)                           |
| `/addon-info`  | Copies a summary of all modules and commands into your clipboard |
| `/find-portal` | Locates nearby Nether portals                                    |

---

## 🔧 Installation

> [!IMPORTANT]
> BE AWARE THAT THE ADDON WON'T ALWAYS BE COMPATIBLE WITH THE LATEST VERSION OF CLICKCRYSTALS.

1. Make sure you have the latest version of [ClickCrystals](https://github.com/ClickCrystals-Development/ClickCrystals)
   installed.
2. Download the **No-oNe-Addon** `.jar` file from
   the [Releases](https://github.com/I-No-oNe/No-oNe-Addon/releases/latest) tab.
3. Place it into the `mods/` folder of your Minecraft instance.
4. Launch the game and toggle modules via the ClickCrystals GUI, The addon Modules will appear in the `Addon` Category.

---
> [!NOTE]
> Some modules may require a **relaunch** of the game to take full effect (e.g. `Mod Menu Disabler`).
---

## 🤝 Contributing

Download the latest ClickCrystals release and add it to your project:

```fish
mkdir -p libs; and set URL (curl -s https://api.github.com/repos/clickcrystals-development/ClickCrystals/releases/latest | string match -rg 'browser_download_url": "(.*\.jar)"' | grep -v "sources" | head -n 1); and curl -L -# "$URL" -o "libs/"(basename "$URL"); and git add .
```

-------

## 📜 License

This project is licensed under the MIT License.  
See the [LICENSE](LICENSE) file for more details.

-------

## Thanks For using!

Don't forget to star the repository if you like it ✨