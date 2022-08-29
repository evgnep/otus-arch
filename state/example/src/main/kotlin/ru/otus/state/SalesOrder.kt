package ru.otus.state

fun main() {
    val player = Player()
    println(player.play())
    println(player.next())
    println(player.next())
    println(player.stop())
    println(player.stop())
}

class Player {
    internal var state: State
    internal var isPlaying = false
    private val playlist: MutableList<String> = ArrayList()
    private var currentTrack = 0

    init {
        state = ReadyState(this)
        isPlaying = true
        for (i in 1..12) {
            playlist.add("Track $i")
        }
    }

    internal fun changeState(state: State) {
        this.state = state
    }

    fun play() = state.onPlay()
    fun stop() = state.onLock()
    fun next() = state.onNext()
    fun prev() = state.onPrevious()

    internal fun startPlayback(): String {
        return "Playing " + playlist[currentTrack]
    }

    internal fun nextTrack(): String {
        currentTrack++
        if (currentTrack > playlist.size - 1) {
            currentTrack = 0
        }
        return "Playing " + playlist[currentTrack]
    }

    internal fun previousTrack(): String {
        currentTrack--
        if (currentTrack < 0) {
            currentTrack = playlist.size - 1
        }
        return "Playing " + playlist[currentTrack]
    }

    internal fun setCurrentTrackAfterStop() {
        currentTrack = 0
    }
}

internal abstract class State(
    val player: Player)
{
    abstract fun onLock(): String
    abstract fun onPlay(): String
    abstract fun onNext(): String
    abstract fun onPrevious(): String
}

internal class LockedState(player: Player) : State(player) {
    init {
        player.isPlaying = false
    }

    override fun onLock(): String {
        return if (player.isPlaying) {
            player.changeState(ReadyState(player))
            "Stop playing"
        } else {
            "Locked..."
        }
    }

    override fun onPlay(): String {
        player.changeState(ReadyState(player))
        return "Ready"
    }

    override fun onNext(): String {
        return "Locked..."
    }

    override fun onPrevious(): String {
        return "Locked..."
    }
}

internal class ReadyState(player: Player) : State(player) {
    override fun onLock(): String {
        player.changeState(LockedState(player))
        return "Locked..."
    }

    override fun onPlay(): String {
        val action: String = player.startPlayback()
        player.changeState(PlayingState(player))
        return action
    }

    override fun onNext(): String {
        return "Locked..."
    }

    override fun onPrevious(): String {
        return "Locked..."
    }
}

internal class PlayingState(player: Player) : State(player) {
    override fun onLock(): String {
        player.changeState(LockedState(player))
        player.setCurrentTrackAfterStop()
        return "Stop playing"
    }

    override fun onPlay(): String {
        player.changeState(ReadyState(player))
        return "Paused..."
    }

    override fun onNext(): String {
        return player.nextTrack()
    }

    override fun onPrevious(): String {
        return player.previousTrack()
    }
}