package ichirika.singing.commands.queue.staff

import ichirika.singing.utils.nullIfStaff
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object ClearQueue : Command(
        description = "Clears the queue entries. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.nullIfStaff("clear the queue").orElse {
                while (queue.count() > 0 && queue.pop()) {
                }
                "The queue has been cleared!"
            }
        }
    }

}