package ichirika.singing.utils

import java.util.*

fun <T> T?.orElse(b: () -> T): T = this ?: b()

fun <T> List<T>.findClosestMatch(match: String, transform: (T) -> String): T? =
        stream().map { it to transform(it) }
                // it needs to contain the match
                .filter { it.second.contains(match, ignoreCase = true) }
                // sort by "closeness"
                .sorted(Comparator.comparingInt {
                    it.second.length - match.length
                })
                // original key
                .findFirst().orElse(null)?.first