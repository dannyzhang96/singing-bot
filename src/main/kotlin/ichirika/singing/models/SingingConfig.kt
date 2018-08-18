package ichirika.singing.models

import nuke.discord.util.Config

object SingingConfig {

    const val FILENAME = "singingbot.cfg"

    private val config = Config(FILENAME)

    val guild = config["guild_id"]
    val roles = config["role_ids"].split(';').map(String::toLong)


}