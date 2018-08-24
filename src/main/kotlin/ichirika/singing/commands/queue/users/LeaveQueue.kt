package ichirika.singing.commands.queue.users

import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object LeaveQueue : Command(
        description = "Leaves the queue."
) {

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