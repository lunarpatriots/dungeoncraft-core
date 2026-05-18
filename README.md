# DungeonCraft Core

[![GitHub Release](https://img.shields.io/github/release/lunarpatriots/dungeoncraft-core.svg?logo=github)](https://github.com/lunarpatriots/dungeoncraft-core/releases)
![github workflow](https://github.com/lunarpatriots/dungeoncraft-core/actions/workflows/ci.yml/badge.svg?branch=master)
[![GitHub last commit](https://img.shields.io/github/last-commit/lunarpatriots/dungeoncraft-core.svg?logo=github)](https://github.com/lunarpatriots/server-auth/commits/develop)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/lunarpatriots/dungeoncraft-core.svg?logo=github)](https://github.com/lunarpatriots/server-auth)
[![GitHub contributors](https://img.shields.io/github/contributors/lunarpatriots/dungeoncraft-core.svg?logo=github)](https://github.com/lunarpatriots/dungeoncraft-core/graphs/contributors)
[![License](https://img.shields.io/github/license/lunarpatriots/dungeoncraft-core.svg?logo=github)](https://github.com/lunarpatriots/dungeoncraft-core/blob/develop/LICENSE)

[![modrinth-badge](https://img.shields.io/modrinth/dt/dungeoncraft-core?label=Modrinth&logo=Modrinth&style=flat-square)](https://modrinth.com/mod/dungeoncraft-core)
[![curseforge-badge](https://img.shields.io/curseforge/dt/1503126?style=flat-square&logo=curseforge&label=CurseForge)](https://curseforge.com/projects/1503126)

Core utility mod to be used mainly for DungeonCraft modpacks.

## Main Features

### Server Auth
   - Players will be required to set a `username` and `password` on the mod's config file
   - Once the player joins a server for the first time, this will trigger a registration
     - Server saves the credentials to a database
     - Server owner then needs to whitelist the user by setting their record to active status
