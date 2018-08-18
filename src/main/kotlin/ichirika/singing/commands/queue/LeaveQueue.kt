package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object LeaveQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            if (!Queue.pop(context.member))
                "You are already not in the queue!"
            else
                """You have left the queue. : (
                  |To join again, type "q! join".
""".trimMargin("|")
        }
    }

}