package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.checkOpen
import ichirika.singing.utils.orElse
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object JoinQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            Queue.checkOpen().orElse {
                if (!Queue.push(context.member))
                    """Woah there tiger! You're already in the queue!
                      |You have to wait until after your turn before you can rejoin.
""".trimMargin("|")
                else
                    """You have joined the queue.
                      |To check how many people are ahead of you, type "q! order".
                      |If you need to leave the queue, type "q! leave".
                      |You're gonna do great!
""".trimMargin("|")
            }
        }
    }

}