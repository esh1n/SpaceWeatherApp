package com.esh1n.utils_android

import java.io.*
import java.nio.charset.Charset

object FileReader {
    fun readFileToString0(inputStream: InputStream): String? {
        return try {
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun readFileToString1(inputStream: InputStream): String? {
        val newLine = System.getProperty("line.separator")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuilder()
        var line: String?
        var flag = false
        while (reader.readLine().also { line = it } != null) {
            result.append(if (flag) newLine else "").append(line)
            flag = true
        }
        return result.toString()
    }

    private fun readFileToString2(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }

    fun readFileToString(inputStream: InputStream): String {
        val bis = BufferedInputStream(inputStream)
        val buf = ByteArrayOutputStream()
        var result: Int = bis.read()
        while (result != -1) {
            buf.write(result)
            result = bis.read()
        }
        return buf.toString("UTF-8")
    }
}
