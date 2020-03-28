package me.interair.wi.word

import java.io.Serializable

/**
 * Reference to document
 */
data class Ref(
        /**
         * Id of referenced doc
         */
        val id: String
) : Serializable