package com.he.chuangwu.libpraticles.utils

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException

class TextResourceReader {

    companion object {
        fun readTextFileFromResource(context: Context, resourceId: Int): String {
            val body = StringBuilder()
            try {
                val openRawResource = context.resources.openRawResource(resourceId)
                val inPutStreamReader = InputStreamReader(openRawResource)
                val bufferResourceReader = BufferedReader(inPutStreamReader)
                var nextLine: String?
                while (bufferResourceReader.readLine().also { nextLine = it } != null) {
                    body.append(nextLine)
                    body.append("\n")
                }
                bufferResourceReader.close()
            } catch (e: IOException) {
                throw RuntimeException("could not open resource: $resourceId ", e)
            } catch (nfe: Resources.NotFoundException) {
                throw RuntimeException("could not found resource: $resourceId ", nfe)
            }
            return body.toString()
        }
    }
}