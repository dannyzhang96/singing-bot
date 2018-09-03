package ichirika.singing.commands.queue.staff

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.utils.channelStore
import ichirika.singing.utils.nullIfStaff
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object UnlinkQueue : Command(
        description = "Unlinks the queue from its text and voice channels if possible. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        context.message.reply {
            append(context.nullIfStaff("unlink the queue").orElse {
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