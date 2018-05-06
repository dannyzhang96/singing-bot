package ichirika.singing.commands.queue

import com.thatsnomoon.kda.extensions.sendEmbedAsync
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.User
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command
import nuke.discord.command.meta.command.PermLevel

sealed class QueueCommands : Command() {

    protected val User.nameWithDiscriminator
        get() = "$asMention ($name#$discriminator)"

    protected fun Message.sendQueueMessage(builder: EmbedBuilder.() -> Unit) {
        channel.sendEmbedAsync {
            setTitle("Queue for: " + guild.name)
            setThumbnail(guild.iconUrl)

            builder()
        }
    }

    object Join : QueueCommands() {
        override fun onInvoke(context: CommandContext) {
            context.message.let { msg ->
                msg.sendQueueMessage {
                    val queue = Queue[msg.guild]
                    val count = queue.push(msg.author)
                    val tag = msg.author.nameWithDiscriminator
                    setDescription(if (count == 1)
                        "Added $tag to the queue"
                    else
                        "Added $tag to the queue ($count entries)")
                }
            }
        }
    }

    object Leave : QueueCommands() {
        override fun onInvoke(context: CommandContext) {
            context.message.let { msg ->
                msg.sendQueueMessage {
                    val user = if (context.tokenizer.hasMore) {
                        context.tokenizer.nextUserMention()?.let {
                            if (context.hasSufficientPermission(PermLevel.MODERATOR)) {
                                context.jda.getUserById(it) ?: msg.author
                            } else {
                                setDescription("Only moderators can remove other people from the queue...")
                                return@sendQueueMessage
                            }
                        } ?: msg.author
                    } else {
                        msg.author
                    }

                    val queue = Queue[msg.guild]
                    if (queue.count(user) == 0) {
                        setDescription("Not present in the queue...")
                    } else {
                        val count = queue.pop(user)
                        val tag = user.nameWithDiscriminator
                        setDescription(if (count == 0)
                            "Removed $tag from the queue"
                        else
                            "Removed $tag from the queue ($count entries left)")
                    }
                }
            }
        }
    }

    object Show : QueueCommands() {
        private const val PAGE_SIZE = 8
        override fun onInvoke(context: CommandContext) {
            context.message.let { msg ->
                msg.sendQueueMessage {
                    val queue = Queue[msg.guild]
                    val count = queue.count()
                    if (count == 0) {
                        setDescription("The queue is empty...")
                    } else {
                        val countDistinct = queue.countDistinct()
                        val list = queue.asImmutableList()

                        setDescription("$count entries from $countDistinct member(s)")

                        val pageCount = (list.size + PAGE_SIZE - 1) / PAGE_SIZE
                        val page = (if (context.tokenizer.hasMore)
                            context.tokenizer.nextInt() ?: 1
                        else 1).coerceIn(1, pageCount)

                        val queueTitle = if (pageCount == 1)
                            "Queue"
                        else
                            "Queue ($page/$pageCount)"

                        var startIndex = ((page - 1) * PAGE_SIZE).coerceAtLeast(0)
                        val endIndex = (page * PAGE_SIZE - 1).coerceAtMost(list.lastIndex)

                        val queueText = list
                                .slice(startIndex..endIndex)
                                .joinToString(separator = "\n") {
                                    "${++startIndex}. ${it.nameWithDiscriminator}"
                                }

                        addField(queueTitle, queueText, false)
                    }
                }

            }
        }
    }

    object Next : QueueCommands() {
        override fun onInvoke(context: CommandContext) {
            context.message.let { msg ->
                msg.sendQueueMessage {
                    val user = if (context.tokenizer.hasMore) {
                        context.tokenizer.nextUserMention()?.let {
                            context.jda.getUserById(it) ?: msg.author
                        } ?: msg.author
                    } else {
                        msg.author
                    }

                    // different user and not a mod
                    if (msg.author != user
                            && !context.hasSufficientPermission(PermLevel.MODERATOR)) {
                        setDescription("Only moderators can next other people from the queue...")
                        return@sendQueueMessage
                    }

                    val queue = Queue[msg.guild]
                    if (queue.count(user) == 0) {
                        setDescription("User not present in the queue...")
                    } else {
                        val count = queue.pop(user)
                        val tag = user.nameWithDiscriminator
                        setDescription(if (count == 0)
                            "Removed $tag from the queue"
                        else
                            "Removed $tag from the queue ($count entries left)")
                    }
                }

            }
        }
    }

}