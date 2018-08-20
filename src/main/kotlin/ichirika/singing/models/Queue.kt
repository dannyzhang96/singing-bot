package ichirika.singing.models

import net.dv8tion.jda.core.entities.Member
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Queue {

    private val queue = LinkedList<Member>()

    var open = AtomicBoolean(false)

    fun contains(member: Member) = synchronized(queue) { member in queue }

    /** Returns true if the member was added (i.e. was not already queued) */
    fun push(member: Member): Boolean = synchronized(queue) {
        if (member in queue) false
        else {
            queue.offerLast(member)
            true
        }
    }

    /** Returns true if the member was removed (i.e. was queued) */
    fun pop(member: Member = queue.peekFirst()): Boolean = synchronized(queue) {
        queue.removeFirstOccurrence(member)
    }

    fun shift(member: Member = queue.peekFirst(), count: Int = 1): Boolean = synchronized(queue) {
        val indexOfMember = queue.indexOf(member)
        val targetIndex = indexOfMember + count

        when {
            indexOfMember == -1
                    || targetIndex !in queue.indices -> false
            else -> {
                queue.removeAt(indexOfMember)
                queue.add(targetIndex, member)
                true
            }
        }

    }

    fun peek(): Member? = synchronized(queue) {
        queue.peekFirst()
    }

    fun asImmutableList() = synchronized(queue) {
        queue.toList()
    }

    fun count() = synchronized(queue) {
        queue.count()
    }

}