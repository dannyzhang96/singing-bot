package ichirika.singing.commands.queue.staff

import ichirika.singing.utils.ifStaff
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object CloseQueue : Command(
        description = "Closes the queue from being entered. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.ifStaff("close the queue").orElse {
                if (queue.open.compareAndSet(true, false))
                    "The queue is now closed. : ("
                else
                    "The queue is already closed."
            }
        }
    }

}