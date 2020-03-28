package me.interair.wi.word

data class WordData(
        override val word: String,
        override val source: Ref
) : StrData