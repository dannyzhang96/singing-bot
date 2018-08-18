package ichirika.singing.commands.queue

import ichirika.singing.models.Queue
import ichirika.singing.utils.replyIfGuild
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command

object OrderQueue : Command() {

    override fun onInvoke(context: CommandContext) {
        context.replyIfGuild {
            val list = Queue.asImmutableList()

            StringBuilder().run {
                append(":arrow_backward:         **QUEUE**         :arrow_forward:\n")

                if (list.isEmpty()) {
                    append("\nEmpty.\n")
                } else {
                    append("\n**Currently:** ")
                    append(list[0].asMention)
                    append('\n')

                    if (list.size > 1) {
                        append("\n**Next:** ")
                        append(list[1].asMention)
                        append('\n')

                        if (list.size > 2) {
                            append('\n')
                            (2..list.lastIndex).map(list::get).forEach {
                                append("~ @")
                                append(it.user.name)
                                append('#')
                                append(it.user.discriminator)
                                append('\n')
                            }
                        }
                    }
                }

                append("\n")
                if (Queue.open.get()) {
                    append(":unlock:  Queue is open.   :unlock:")
                } else {
                    append(":lock: Queue is closed. :lock:")
                }

                toString()
            }
        }
    }

}