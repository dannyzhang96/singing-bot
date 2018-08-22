package ichirika.singing.commands.queue.users

import ichirika.singing.commands.SCommand
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object NextQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.ifStaffOrFirst("next the queue").orElse {
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