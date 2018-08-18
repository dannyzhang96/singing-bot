package ichirika.singing

import ichirika.singing.commands.queue.CloseQueue
import ichirika.singing.commands.queue.JoinQueue
import ichirika.singing.commands.queue.LeaveQueue
import ichirika.singing.commands.queue.NextQueue
import ichirika.singing.commands.queue.OpenQueue
import ichirika.singing.commands.queue.OrderQueue
import ichirika.singing.commands.queue.SkipQueue
import ichirika.singing.models.SingingConfig
import nuke.discord.bot.runBot
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command
import nuke.discord.command.meta.selectors.PrefixSelector

fun main(args: Array<String>) {

    println(System.getenv())

    runBot {
        configName = SingingConfig.FILENAME

        commands("q!", PrefixSelector) {
            it["open"] = OpenQueue
            it["join"] = JoinQueue
            it["leave"] = LeaveQueue
            it["skip"] = SkipQueue
            it["next"] = NextQueue
            it["order"] = OrderQueue
            it["close"] = CloseQueue

            it.fallback(object : Command() {
                override fun onInvoke(context: CommandContext) {
                }
            })
        }
    }

}