package ichirika.singing.utils

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.models.Queue
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.entities.Message
import nuke.discord.command.meta.CommandContext

fun <T> T?.orElse(b: () -> T): T = this ?: b()

fun CommandContext.replyIfGuild(builder: (Message) -> Any?) {
    if (this.guild.id == SingingConfig.guild) {
        this.message.reply {
            synchronized(Queue) {
                builder(message)
            }
        }
    }
}