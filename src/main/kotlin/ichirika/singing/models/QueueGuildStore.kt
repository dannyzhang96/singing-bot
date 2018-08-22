package ichirika.singing.models

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel


object QueueGuildStore {

    private val storeByGuild = mutableMapOf<Guild, QueueChannelStore>()

    operator fun get(guild: Guild) = storeByGuild.getOrPut(guild) { QueueChannelStore(guild) }

    operator fun get(text: TextChannel) = this[text.guild][text]
    operator fun get(voice: VoiceChannel) = this[voice.guild][voice]

    fun init(client: JDA) {
        QueueStore.listGuilds(client).forEach {
            storeByGuild[it] = QueueChannelStore(it).apply { init(client) }
        }
    }

}
