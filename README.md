# Server Auth
[![GitHub last commit](https://img.shields.io/github/last-commit/lunarpatriots/server-auth.svg?logo=github)](https://github.com/lunarpatriots/server-auth/commits/develop)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/lunarpatriots/server-auth.svg?logo=github)](https://github.com/lunarpatriots/server-auth)
[![GitHub contributors](https://img.shields.io/github/contributors/lunarpatriots/server-auth.svg?logo=github)](https://github.com/lunarpatriots/server-auth/graphs/contributors)
[![License](https://img.shields.io/github/license/lunarpatriots/server-auth.svg?logo=github)](https://github.com/lunarpatriots/server-auth/blob/develop/LICENSE)

Simple player registration/whitelisting for the Fabric modloader. Specifically built with offline servers in mind.

## Main Features
- Players will be required to set a `username` and `password` in the config file
- Once the player joins a server for the first time, this will trigger a registration
  - Server saves the credentials to a database
  - Server owner then needs to whitelist the user by setting their record to active status
