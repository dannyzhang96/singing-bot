package ichirika.singing.commands.queue.staff

import ichirika.singing.commands.SCommand
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object OpenQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.ifStaff("open the queue").orElse {
                if (queue.open.compareAndSet(false, true))
                    """The queue is now open.
                      |Please type "q! join" to secure your spot before it closes again!
""".trimMargin("|")
                else
                    "The queue is already open."
            }
        }
    }

}