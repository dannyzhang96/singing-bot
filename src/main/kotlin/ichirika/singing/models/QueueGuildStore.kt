package ichirika.singing.models

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel


object QueueGuildStore {

    private val storeByGuild = mutableMapOf<Guild, QueueChannelStore>()

    operator fun get(guild: Guild) = storeByGuild.getOrPut(guild, ::QueueChannelStore)

    operator fun get(text: TextChannel) = this[text.guild][text]
    operator fun get(voice: VoiceChannel) = this[voice.guild][voice]

}
