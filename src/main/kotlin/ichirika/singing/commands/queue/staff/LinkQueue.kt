package ichirika.singing.commands.queue.staff

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.commands.SCommand
import ichirika.singing.models.QueueChannelStore.OpResult
import ichirika.singing.utils.orElse
import nuke.discord.command.meta.CommandContext

object LinkQueue : SCommand() {

    override fun onInvoke(context: CommandContext) {
        context.message.reply {
            append(context.ifStaff("link the queue").orElse {
                val text = context.message.textChannel
                val voice = context.member.voiceState.channel

                when {
                    voice == null ->
                        "You must be connected to the voice channel the queue will be linked to."
                    context.channelStore.link(text, voice) and OpResult.TEXT_AND_VOICE_ALREADY_LINKED != 0 ->
                        "These text and voice channels have been relinked to a queue."
                    else ->
                        "These text and voice channels have been linked to a queue."
                }
            })
        }
    }

}