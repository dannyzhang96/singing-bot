package ichirika.singing.commands.queue.users

import ichirika.singing.utils.checkEmpty
import ichirika.singing.utils.ifStaffOrFirst
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object RejoinQueue : Command(
        description = "Nexts and rejoins the queue if possible. Used by the member on top of the queue."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.ifStaffOrFirst("rejoin the queue").orElse {
                queue.checkEmpty().orElse {
                    val prevTop = queue.peek()!!.also { queue.pop() }

                    if (!queue.push(context.member)) {
                        """Woah there tiger! You're already in the queue!
                          |You have to wait until after your turn before you can rejoin.
""".trimMargin("|")
                    } else {
                        """Thank you for performing, ${prevTop.asMention} !
                          |You've been put back into the queue.
                          |Next up we have ${queue.peek()!!.asMention} !
""".trimMargin("|")
                    }
                }
            }
        }
    }

}