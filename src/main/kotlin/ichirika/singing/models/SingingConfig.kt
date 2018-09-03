package ichirika.singing.models

import nuke.discord.util.Config

object SingingConfig {

    const val FILENAME = "singingbot.cfg"

    private val config = Config(FILENAME)

    val roles = config["role_ids"].split(';').map(String::toLong)

    val dbConnectionUrl = config["database_url"]


}