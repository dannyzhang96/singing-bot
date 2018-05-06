package ichirika.singing

import ichirika.singing.commands.queue.QueueCommands
import nuke.discord.bot.runBot

fun main(args: Array<String>) {

    runBot {
        configName = "singingbot.cfg"

        commands("!") {
            it("q") {
                it["join"] = QueueCommands.Join
                it["leave"] = QueueCommands.Leave
                it["show"] = QueueCommands.Show
                it["next"] = QueueCommands.Next
            }
        }
    }

}