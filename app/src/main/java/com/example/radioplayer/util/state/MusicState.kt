package com.example.radioplayer.util.state

sealed class MusicState(val musicTitle: String) {
    class Favorite(musicTitle: String): MusicState(musicTitle)
    class Usual(musicTitle: String): MusicState(musicTitle)

    /**
     * Undefined state of music
     * */
    class Unknown(musicTitle: String): MusicState(musicTitle)

    /**
     * Title is missing (isBlank) or is not music, such as "ads" or "news"
     * */
    class Other(title: String = String()): MusicState(title)

    fun isMusic(): Boolean {
        return when(this) {
            is Favorite -> true
            is Usual -> true
            is Unknown -> false
            is Other -> false
        }
    }
}