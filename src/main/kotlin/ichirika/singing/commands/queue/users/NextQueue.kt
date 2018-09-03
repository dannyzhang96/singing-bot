package ichirika.singing.commands.queue.users

import ichirika.singing.utils.checkEmpty
import ichirika.singing.utils.nullIfStaffOrFirst
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object NextQueue : Command(
        description = "Advances the queue to the next person if possible. Used by the member on top of the queue."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.nullIfStaffOrFirst("next the queue").orElse {
                queue.checkEmpty().orElse {
                    val prevTop = queue.peek()!!.also { queue.pop() }

                    if (queue.count() < 1) {
                        """Thank you for performing, ${prevTop.asMention} !
                          |The queue is now empty.
""".trimMargin("|")
                    } else {
                        """Thank you for performing, ${prevTop.asMention} !
                          |Next up we have ${queue.peek()!!.asMention} !
""".trimMargin("|")
                    }
                }
            }
        }
    }

}