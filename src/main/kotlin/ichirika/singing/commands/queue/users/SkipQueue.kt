package ichirika.singing.commands.queue.users

import ichirika.singing.utils.checkEmpty
import ichirika.singing.utils.nullIfStaffOrFirst
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object SkipQueue : Command(
        description = "Skips the top entry, moving it back into the queue, possibly by multiple places. Used by the member on top of the queue."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.nullIfStaffOrFirst("skip a queue entry").orElse {
                queue.checkEmpty().orElse emptyElse@{
                    val count = if (!context.tokenizer.hasMore) {
                        // skip -> skip top entry once
                        1
                    } else {
                        // skip N -> skip top entry, N times
                        context.tokenizer.nextInt() ?: 1
                    }

                    val member = queue.peek()!!

                    if (!queue.contains(member))
                        "This user is not in the queue."
                    else if (!queue.shift(member, count))
                        "You can't skip that far back."
                    else
                        """${member.asMention} has been skipped.
                          |Next up we have ${queue.peek()!!.asMention} !
""".trimMargin("|")
                }
            }
        }
    }

}