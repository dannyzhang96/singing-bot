package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.checkEmpty
import ichirika.singing.utils.checkRoles
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object SkipQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            context.checkRoles("skip a queue entry").orElse {
                Queue.checkEmpty().orElse emptyElse@{
                    val count = if (!context.tokenizer.hasMore) {
                        // skip -> skip top entry once
                        1
                    } else {
                        // skip N -> skip top entry, N times
                        context.tokenizer.nextInt() ?: 1
                    }

                    val member = Queue.peek()!!

                    if (!Queue.contains(member))
                        "This user is not in the queue."
                    else if (!Queue.shift(member, count))
                        "You can't skip that far back."
                    else
                        """${member.asMention} has been skipped.
                          |Next up we have ${Queue.peek()!!.asMention} !
""".trimMargin("|")
                }
            }
        }
    }

}