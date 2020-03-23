package me.interair.wi.gw.docs

import java.io.File

/**
 * ToDo: implement sharding by folders
 */
class DocsStorage(val baseFileLocation: String) {

    fun get(id: String): File? {
        return File(baseFileLocation, id)
    }

    fun getAvailable(): List<File> {
        return File(baseFileLocation).listFiles().asList()
    }

    fun store(fileName: String, file: ByteArray): File {
        val storedFile = File(baseFileLocation, fileName)
        storedFile.writeBytes(file)
        return storedFile
    }
}