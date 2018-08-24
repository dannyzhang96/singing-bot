package ichirika.singing.models

import nuke.discord.util.Config

object SingingConfig {

    const val FILENAME = "singingbot.cfg"
    const val DB_DRIVER = "com.mysql.jdbc.Driver"

    private val config = Config(FILENAME)

    val roles = config["role_ids"].split(';').map(String::toLong)

    val dbConnectionUrl = config["database_url"]


}