package ichirika.singing.commands.staff

import ichirika.singing.utils.findRole
import ichirika.singing.utils.isInStaff
import net.dv8tion.jda.core.entities.Role
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object MentionRole : Command(
        description = "Mentions a role, regardless of its mentionable status. Deletes command after use. Used by staff."
) {

    override fun onInvoke(context: CommandContext) {
        if (context.isInStaff) {
            val roleName = context.tokenizer.tail()
            val role = context.guild.findRole(roleName)
            if (role == null) {
                context.replyFail("there is no role by that name.")
                return
            }

            if (role.isMentionable) {
                context.mention(role)
            } else {
                role.manager.setMentionable(true)
                context.mention(role)
                role.manager.setMentionable(false)
            }
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun CommandContext.mention(role: Role) {
        channel.sendMessage(role.asMention)
        message.delete()
    }

}