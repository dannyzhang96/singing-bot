package ichirika.singing.utils

import com.thatsnomoon.kda.extensions.reply
import ichirika.singing.models.Queue
import ichirika.singing.models.QueueGuildStore
import nuke.discord.command.meta.CommandContext

val CommandContext.channelStore get() = QueueGuildStore[guild]

val CommandContext.queue get() = channelStore[message.textChannel]

fun CommandContext.replyIfLinked(block: (Queue) -> String) {
    synchronized(QueueGuildStore) {
        queue?.let {
            message.reply { append(block(it)) }
        }
    }
}

fun CommandContext.nullIfStaffOrFirst(action: String = "do this command") = when {
    queue?.peek() == member -> null
    isInStaff -> null
    else -> "Only the member on top of the queue is allowed to $action."
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