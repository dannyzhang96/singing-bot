package ichirika.singing.models

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

object QueueStore : Table(name = "queue_store") {

    private val guildId = long("guild").primaryKey()
    private val textId = long("text_channel").primaryKey().uniqueIndex()
    private val voiceId = long("voice_channel").primaryKey().uniqueIndex()

    /** Shorthands */

    fun insert(guild: Guild, text: TextChannel, voice: VoiceChannel) {
        QueueStore.insert {
            it[guildId] = guild.idLong
            it[textId] = text.idLong
            it[voiceId] = voice.idLong
        }
    }

    fun delete(guild: Guild, text: TextChannel, voice: VoiceChannel) {
        QueueStore.deleteWhere {
            (QueueStore.guildId eq guild.idLong) and
                    ((QueueStore.textId eq text.idLong) or
                            (QueueStore.voiceId eq voice.idLong))
        }
    }

    fun listGuilds(client: JDA) =
            QueueStore.selectAll()
                    .groupBy(QueueStore.guildId)
                    .map {
                        client.getGuildById(it[guildId])
                    }

    fun listChannels(client: JDA, guild: Guild) =
            QueueStore.select { QueueStore.guildId eq guild.idLong }
                    .map {
                        client.getTextChannelById(it[textId]) to
                                client.getVoiceChannelById(it[voiceId])
                    }

}