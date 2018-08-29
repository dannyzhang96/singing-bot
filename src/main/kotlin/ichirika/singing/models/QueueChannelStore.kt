package ichirika.singing.models

import ichirika.singing.models.QueueChannelStore.OpResult.ALL_CLEAR
import ichirika.singing.models.QueueChannelStore.OpResult.TEXT_ALREADY_LINKED
import ichirika.singing.models.QueueChannelStore.OpResult.VOICE_ALREADY_LINKED
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import org.jetbrains.exposed.sql.transactions.transaction

class QueueChannelStore(private val guild: Guild) {

    object OpResult {
        const val ALL_CLEAR = 0b00
        const val TEXT_ALREADY_LINKED = 0b01
        const val VOICE_ALREADY_LINKED = 0b10
        const val TEXT_AND_VOICE_ALREADY_LINKED = 0b11
    }

    private data class Entry(val text: TextChannel,
                             val voice: VoiceChannel,
                             val queue: Queue = Queue())

    private val channelsByText = mutableMapOf<TextChannel, Entry>()
    private val channelsByVoice = mutableMapOf<VoiceChannel, Entry>()

    operator fun get(text: TextChannel): Queue? = channelsByText[text]?.queue
    operator fun get(voice: VoiceChannel): Queue? = channelsByVoice[voice]?.queue

    operator fun contains(text: TextChannel) = text in channelsByText
    operator fun contains(voice: VoiceChannel) = voice in channelsByVoice

    fun init(client: JDA) {
        QueueStore.listChannels(client, guild).forEach { (text, voice) ->
            Entry(text, voice, Queue()).let {
                channelsByText[text] = it
                channelsByVoice[voice] = it
            }
        }
    }

    /** Link a queue to a text and voice channel, returns OpResult */
    fun link(text: TextChannel, voice: VoiceChannel): Int {
        var result = ALL_CLEAR

        if (text in channelsByText) {
            result = (result or TEXT_ALREADY_LINKED)
            unlink(text)
        }

        if (voice in channelsByVoice) {
            result = (result or VOICE_ALREADY_LINKED)
            unlink(voice)
        }

        Entry(text, voice, Queue()).let {
            channelsByText[text] = it
            channelsByVoice[voice] = it

            transaction {
                QueueStore.insert(guild, text, voice)
            }
        }

        return result
    }

    fun unlink(text: TextChannel) = channelsByText[text]?.let { unlink(text, it.voice) }
            ?: false

    fun unlink(voice: VoiceChannel) = channelsByVoice[voice]?.let { unlink(it.text, voice) }
            ?: false

    private fun unlink(text: TextChannel, voice: VoiceChannel) = transaction {
        channelsByText[text]
                ?.takeIf { it.voice == voice }
                ?.let {
                    channelsByText.remove(text)
                    channelsByVoice.remove(voice)

                    QueueStore.delete(guild, text, voice)
                    true
                } ?: false
    }

}
