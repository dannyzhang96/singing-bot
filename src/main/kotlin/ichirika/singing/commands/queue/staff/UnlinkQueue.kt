package ichirika.singing.commands.queue.staff

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.commands.SCommand
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object UnlinkQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.message.reply {
            append(context.ifStaff("unlink the queue").orElse {
                val text = context.message.textChannel

                when {
                    context.channelStore.unlink(text) ->
                        "This text channel has been unlinked from the queue and its voice channel."
                    else ->
                        "This text channel is not linked to a queue."
                }
            })
        }
    }

}