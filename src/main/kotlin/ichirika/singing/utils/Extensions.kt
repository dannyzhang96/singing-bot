package ichirika.singing.utils

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.models.Queue
import ichirika.singing.models.QueueGuildStore
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.Permission
import nuke.discord.command.meta.CommandContext
import nuke.discord.util.discord.hasRoleForGuild

fun <T> T?.orElse(b: () -> T): T = this ?: b()

val CommandContext.channelStore get() = QueueGuildStore[guild]

val CommandContext.queue get() = channelStore[message.textChannel]

fun CommandContext.replyIfLinked(block: (Queue) -> String) {
    synchronized(QueueGuildStore) {
        queue?.let {
            message.reply { append(block(it)) }
        }
    }
}

fun CommandContext.ifStaff(action: String = "do this command") = when {
    SingingConfig.roles.any(member::hasRoleForGuild)
            || member.hasPermission(Permission.MANAGE_SERVER) -> null
    else -> "Only staff members are allowed to $action."
}

fun CommandContext.ifStaffOrFirst(action: String = "do this command") =
        ifStaff(action)?.let {
            if (queue?.peek() == member) null
            else it
        }

fun Queue.checkEmpty() =
        if (count() > 0) null
        else
            "Sorry, the queue is empty!"

fun Queue.checkOpen() =
        if (open.get()) null
        else
            """Sorry, the queue is locked!
              |You can only join the queue after a staff member has opened it. :(
""".trimMargin("|")
