package ichirika.singing.commands

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.models.Queue
import ichirika.singing.models.QueueGuildStore
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.Permission
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command
import nuke.discord.command.meta.command.PermLevel
import nuke.discord.util.discord.hasRoleForGuild

abstract class SCommand(
        val description: String = "There is no command description",
        val usage: String = "",
        requiredPermission: PermLevel = PermLevel.USER
) : Command(requiredPermission) {

    val CommandContext.channelStore get() = QueueGuildStore[guild]

    fun CommandContext.replyIfLinked(block: (Queue) -> String) {
        synchronized(QueueGuildStore) {
            channelStore[message.textChannel]?.let { queue ->
                message.reply { append(block(queue)) }
            }
        }
    }

    fun CommandContext.checkRoles(action: String = "do this command") = when {
        SingingConfig.roles.any(member::hasRoleForGuild)
                || member.hasPermission(Permission.MANAGE_SERVER) -> null
        else -> "Only staff members are allowed to $action."
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

}
