package ichirika.singing.utils

fun <T> T?.orElse(b: () -> T): T = this ?: b()
