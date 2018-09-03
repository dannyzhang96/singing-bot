package ichirika.singing.utils

import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role
import nuke.discord.command.meta.CommandContext
import nuke.discord.util.discord.hasRoleForGuild

val CommandContext.isInStaff
    get() = SingingConfig.roles.any(member::hasRoleForGuild)
                || member.hasPermission(Permission.MANAGE_SERVER)

fun CommandContext.nullIfStaff(action: String = "do this command") = when {
    isInStaff -> null
    else -> "Only staff members are allowed to $action."
}

fun Guild.findRole(match: String) =
        roles.findClosestMatch(match, Role::getName)
