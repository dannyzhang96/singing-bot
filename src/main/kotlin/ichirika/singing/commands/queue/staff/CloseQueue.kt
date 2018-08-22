package ichirika.singing.commands.queue.staff

import ichirika.singing.commands.SCommand
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object CloseQueue : SCommand() {

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