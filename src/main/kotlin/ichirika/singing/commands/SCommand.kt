package ichirika.singing.commands

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.models.Queue
import ichirika.singing.models.QueueGuildStore
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.MessageBuilder
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

    fun CommandContext.replyIfLinked(block: MessageBuilder.(Queue) -> Unit) {
        synchronized(QueueGuildStore) {
            QueueGuildStore[guild][message.textChannel]?.let { queue ->
                message.reply { block(queue) }
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
