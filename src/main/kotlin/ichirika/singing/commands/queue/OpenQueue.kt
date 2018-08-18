package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.checkRoles
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object OpenQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            context.checkRoles("open the queue").orElse {
                if (Queue.open.compareAndSet(false, true))
                    """The queue is now open.
                      |Please type "q! join" to secure your spot before it closes again!
""".trimMargin("|")
                else
                    "The queue is already open."
            }
        }
    }

}