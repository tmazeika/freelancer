package me.mazeika.freelancer.model.notify

class Notifier {
    private val handlers: MutableList<() -> Unit> = mutableListOf()

    operator fun plusAssign(block: () -> Unit) {
        handlers += block
    }

    internal operator fun invoke() {
        handlers.forEach { it() }
    }
}
