package ichirika.singing.utils

import java.util.*

fun <T> T?.orElse(b: () -> T): T = this ?: b()

fun <T> List<T>.findClosestMatch(match: String, transform: (T) -> String): T? =
        stream().map { it to transform(it) }
                .filter { it.second.contains(match) } // it needs to contain the match
                .sorted(Comparator.comparingInt {
                    it.second.length - match.length // sort by "closeness"
                })
                .findFirst().orElse(null)?.first // original key