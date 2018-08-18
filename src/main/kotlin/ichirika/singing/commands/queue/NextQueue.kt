package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.checkEmpty
import ichirika.singing.utils.checkRoles
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object NextQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            context.checkRoles("next the queue").orElse {
                Queue.checkEmpty().orElse {
                    val prevTop = Queue.peek()!!.also { Queue.pop() }

                    if (Queue.count() < 1) {
                        """Thank you for performing, ${prevTop.asMention} !
                          |The queue is now empty.
""".trimMargin("|")
                    } else {
                        """Thank you for performing, ${prevTop.asMention} !
                          |Next up we have ${Queue.peek()!!.asMention} !
""".trimMargin("|")
                    }
                }
            }
        }
    }

}