package ichirika.singing.commands.queue.users

import ichirika.singing.commands.SCommand
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object JoinQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfLinked { queue ->
            queue.checkOpen().orElse {
                if (!queue.push(context.member))
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