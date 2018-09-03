package ichirika.singing.commands.queue.staff

import ichirika.singing.utils.nullIfStaff
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfLinked
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object RemoveQueue : Command(
        description = "Removes from the queue. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            context.nullIfStaff("remove someone from the queue").orElse {
                val userMentionId = context.tokenizer.nextUserMention()
                if (userMentionId == null)
                    "You need to mention a user to be removed from the queue!"
                else context.guild.getMemberById(userMentionId).let {
                    if (!queue.pop(it))
                        "This member is not in the queue!"
                    else
                        "${it.asMention} has been removed from the queue. : ("
                }
            }
        }
    }

}