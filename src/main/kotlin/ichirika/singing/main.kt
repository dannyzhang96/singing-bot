package ichirika.singing

import ichirika.singing.commands.queue.staff.CloseQueue
import ichirika.singing.commands.queue.staff.LinkQueue
import ichirika.singing.commands.queue.staff.OpenQueue
import ichirika.singing.commands.queue.staff.UnlinkQueue
import ichirika.singing.commands.queue.users.JoinQueue
import ichirika.singing.commands.queue.users.LeaveQueue
import ichirika.singing.commands.queue.users.NextQueue
import ichirika.singing.commands.queue.users.OrderQueue
import ichirika.singing.commands.queue.users.SkipQueue
import ichirika.singing.models.QueueGuildStore
import ichirika.singing.models.QueueStore
import ichirika.singing.models.SingingConfig
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent
import nuke.discord.bot.runBot
import nuke.discord.command.meta.CommandContext
import nuke.discord.command.meta.command.Command
import nuke.discord.command.meta.selectors.PrefixSelector
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {

    Database.connect(
            url = SingingConfig.dbUrl,
            user = SingingConfig.dbUsername,
            password = SingingConfig.dbPassword,
            driver = SingingConfig.DB_DRIVER
    )

    transaction {
        create(QueueStore)
    }

    runBot {
        configName = SingingConfig.FILENAME

        commands("q!", PrefixSelector) {
            it["join"] = JoinQueue
            it["leave"] = LeaveQueue
            it["order"] = OrderQueue

            it["skip"] = SkipQueue
            it["next"] = NextQueue

            it["open"] = OpenQueue
            it["close"] = CloseQueue

            it["link"] = LinkQueue
            it["unlink"] = UnlinkQueue

            it.fallback(object : Command() {
                override fun onInvoke(context: CommandContext) {
                }
            })
        }

        eventListener(object {
            @SubscribeEvent
            fun onReady(event: ReadyEvent) {
                QueueGuildStore.init(event.jda)
            }
        })
    }

}
