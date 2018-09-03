package ichirika.singing.commands.queue.staff

import ichirika.singing.utils.nullIfStaff
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object OpenQueue : Command(
        description = "Opens up the queue for people to enter. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.nullIfStaff("open the queue").orElse {
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