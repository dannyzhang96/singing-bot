package ichirika.singing.commands.queue

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User
import java.util.*

class Queue private constructor() {

    companion object {
        private val queues: MutableMap<Guild, Queue> =
                Collections.synchronizedMap(linkedMapOf())

        operator fun get(guild: Guild): Queue = queues.getOrPut(guild, ::Queue)
    }

    private val map = mutableMapOf<User, Int>()
    private val queue = LinkedList<User>()

    fun contains(user: User) = synchronized(this) { user in map }

    fun push(user: User): Int = synchronized(this) {
        map.getOrPut(user) { 0 }.let { map.put(user, it + 1) }
        queue.addLast(user)
        queue.size
    }

    fun pop(user: User): Int = synchronized(this) {
        val count = map.getOrDefault(user, 0)
        if (count > 1) {
            map[user] = count - 1
        } else {
            map.remove(user)
        }
        queue.removeFirstOccurrence(user)
        queue.size
    }

    fun asImmutableList() = synchronized(this) { queue.toList() }

    fun count() = synchronized(this) { queue.count() }
    fun countDistinct() = synchronized(this) { map.count() }

    fun count(user: User) = synchronized(this) {
        map.getOrDefault(user, 0)
    }

}