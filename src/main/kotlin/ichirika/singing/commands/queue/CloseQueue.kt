package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.checkRoles
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object CloseQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            context.checkRoles("close the queue").orElse {
                if (Queue.open.compareAndSet(true, false))
                    "The queue is now closed. : ("
                else
                    "The queue is already closed."
            }
        }
    }

}