# ![pageres](http://puu.sh/p8vKT/cc7c549307.png)

There is no planned release date for the Samaritan Source Code.
It's currently in Closed Beta Testing. (Beta 2.0)

Samaritan is a fancy multifunction bot for Discord (http://discordapp.com). It has first been (badly by me) done (1.0), and is now being remade clearly: 2.0. It supports things like Music, CleverBot implementation, Advanced Command System, etc. In Open Beta.

It's still In Development! Please report bugs!

## Made with...

- Discord API: JDA.
- JDK: 8.
- Build System: Gradle.

## Installation

On your IDE:
- Git clone the repo.
- Import build.gradle

To run it:
- Download the jar: 
- Run it with java -jar.
- Configure samaritan.properties
- Run again.

## Usage

- Define admin in samaritan.properties, can use all commands.
- Use £help to see list of commands.

## CleverBot implementation.

Samaritan implements CleverBot.
To speak with Samaritan:
- Start a private message with the bot.

## UI

You surely saw in the config things about Ui.
What are those?
Samaritan implements a WebSocket server, that you can enable/disable.
To work with a web interface made originally by Rodrigo Graça, modified by me to receive the WebSockets.
Modified UI Repo: https://github.com/iSach/SamaritanUI

To install and use that:
- Turn on and configure the WebSocket server in samaritan.properties.
- Restart Samaritan.
- On your website folder, put somewhere the SamaritanUI folder.
- Configure in samaritan.js the port and the hostname.
- Go on your website.
- Go on discord, and execute that command: £send Hello world !
- "HELLO" -> "WORLD" -> "!" will appear on the web interface.

## Team

[![Sacha "iSach" Lewin](https://avatars3.githubusercontent.com/u/13520261?v=3&s=460)](https://isach.be) |
---|
[Sacha "iSach" Lewin](https://sindresorhus.com) |


## License

MIT

Free Software, Hell Yeah!
