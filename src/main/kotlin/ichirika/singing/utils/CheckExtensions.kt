package ichirika.singing.utils

import ichirika.singing.models.Queue
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.Permission
import nuke.discord.command.meta.CommandContext
import nuke.discord.util.discord.hasRoleForGuild

fun Queue.checkEmpty() =
        if (Queue.count() > 0) null
        else
            "Sorry, the queue is empty!"

fun Queue.checkOpen() =
        if (Queue.open.get()) null
        else
            """Sorry, the queue is locked!
                  |You can only join the queue after a staff member has opened it. :(
""".trimMargin("|")

fun CommandContext.checkRoles(action: String = "do this command") =
        when {
            SingingConfig.roles.any(member::hasRoleForGuild)
                || member.hasPermission(Permission.MANAGE_SERVER) -> null
            else -> "Only staff members are allowed to $action."
        }