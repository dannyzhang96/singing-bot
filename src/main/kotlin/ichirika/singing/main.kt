package ichirika.singing

import ichirika.singing.commands.queue.staff.CloseQueue
import ichirika.singing.commands.queue.staff.OpenQueue
import ichirika.singing.commands.queue.users.JoinQueue
import ichirika.singing.commands.queue.users.LeaveQueue
import ichirika.singing.commands.queue.users.NextQueue
import ichirika.singing.commands.queue.users.OrderQueue
import ichirika.singing.commands.queue.users.SkipQueue
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