package ichirika.singing.commands.queue.users

import ichirika.singing.commands.SCommand
import nuke.discord.command.meta.CommandContext

object LeaveQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            if (!queue.pop(context.member))
                "You are already not in the queue!"
            else
                """You have left the queue. : (
                  |To join again, type "q! join".
""".trimMargin("|")
        }
    }

}