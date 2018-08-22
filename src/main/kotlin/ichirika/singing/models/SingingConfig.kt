package ichirika.singing.models

import nuke.discord.util.Config

object SingingConfig {

    const val FILENAME = "singingbot.cfg"
    const val DB_DRIVER = "com.mysql.jdbc.Driver"

    private val config = Config(FILENAME)

    val guild = config["guild_id"]
    val roles = config["role_ids"].split(';').map(String::toLong)

    val dbUrl = config["jdbc_url"]
    val dbUsername = config["jdbc_username"]
    val dbPassword = config["jdbc_password"]

}