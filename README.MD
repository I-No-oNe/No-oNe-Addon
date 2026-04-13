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

| Name                  | Description                                                 |
|-----------------------|-------------------------------------------------------------|
| Gui Features Disabler | Cancels the annoying things inside the ClickCrystals GUI    |
| Infinite Chat         | Removes the 256-character limit from chat                   |
| Cape Disabler         | Disables ClickCrystals capes for vanilla cape               |
| Middle Click Ping     | Pings a player in chat when you middle-click on them        |
| Loot Keeper           | Prevents specific items from being dropped or lost          |
| No Potions Render     | Hides potion effects from the Potion HUD                    |
| Hit Color             | Changes the hit flash color of entities                     |
| Dm Spammer            | Spams private messages to selected players                  |
| Mod Menu Disabler     | Hides ClickCrystals from mod menu, **REQUIRES RESTART**     |
| Upside Down           | Renders all players and entities upside down                |
| Prevent               | Blocks accidental glowstone or anchor placements            |
| Anti Off Hand         | Prevents off-hand swapping under specific conditions        |
| Ghost Interactions    | Prevents sending packets when closing containers            |
| Safe Walk             | Automatically sneaks at the edge of blocks                  |
| Name Changer          | Changes the displayed name of the player (client-side only) |

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