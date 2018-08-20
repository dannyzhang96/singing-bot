package ichirika.singing.models

import ichirika.singing.models.QueueChannelStore.OpResult.ALL_CLEAR
import ichirika.singing.models.QueueChannelStore.OpResult.TEXT_ALREADY_LINKED
import ichirika.singing.models.QueueChannelStore.OpResult.VOICE_ALREADY_LINKED
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel

class QueueChannelStore {

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

    /** Link a queue to a text and voice channel, returns OpResult */
    fun link(text: TextChannel, voice: VoiceChannel): Int {
        var result = ALL_CLEAR

        if (text in channelsByText) {
            result = (result or TEXT_ALREADY_LINKED)
            channelsByText.remove(text)?.voice?.let(channelsByVoice::remove)
        }

        if (voice in channelsByVoice) {
            result = (result or VOICE_ALREADY_LINKED)
            channelsByVoice.remove(voice)?.text?.let(channelsByText::remove)
        }

        Entry(text, voice, Queue()).let {
            channelsByText[text] = it
            channelsByVoice[voice] = it
        }

        return result
    }

    fun unlink(text: TextChannel) = channelsByText[text]?.let { unlink(text, it.voice) }
    fun unlink(voice: VoiceChannel) = channelsByVoice[voice]?.let { unlink(it.text, voice) }

    fun unlink(text: TextChannel, voice: VoiceChannel) =
            channelsByText[text]
                    ?.takeIf { it.voice == voice }
                    ?.let {
                        channelsByText.remove(text)
                        channelsByVoice.remove(voice)
                        true
                    } ?: false

}
