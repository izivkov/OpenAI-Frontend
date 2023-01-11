package org.avmedia.openaifrontend

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.avmedia.openaifrontend.MainActivity.Companion.applicationContext
import org.avmedia.openaifrontend.utils.LocalDataStorage
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

object OpenAIConnection {
    private lateinit var apiKey:String

    private fun submitRequestGetData(question: String): String {
        apiKey = LocalDataStorage.get("apikey", "", applicationContext()) as String

        val url = URL("https://api.openai.com/v1/completions")
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.requestMethod = "POST"

        // Add request header
        httpClient.setRequestProperty("Content-Type", "application/json")
        httpClient.setRequestProperty(
            "Authorization",
            "Bearer $apiKey"
        )

        // Build the body of the request
        val body = JSONObject()
        body.put("model", "text-davinci-003")
        body.put("prompt", question)
        body.put("temperature", 0.7)
        body.put("max_tokens", 1024)
        body.put("frequency_penalty", 0.0)
        body.put("presence_penalty", 0.0)

        // Send the request
        httpClient.doOutput = true
        val outputStream = httpClient.outputStream
        outputStream.write(body.toString().toByteArray())
        outputStream.flush()
        outputStream.close()

        // Get the response
        return try {
            val inputStream = httpClient.inputStream
            val responseBody = inputStream.bufferedReader().readText()
            val text = ((JSONObject(responseBody).get("choices") as JSONArray)[0] as JSONObject).get("text") as String
            "{error: \"\", text:\"${cleanText(text)}\"}"
        } catch (e: FileNotFoundException) {
            "{error: \"Cannot get the answer. Are you sure you have a valid API Key?\", text:\"\"}"
        }
    }

    private fun submitRequestGetImage(question: String): String {
        apiKey = LocalDataStorage.get("apikey", "", applicationContext()) as String
        val url = URL("https://api.openai.com/v1/images/generations")
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.requestMethod = "POST"

        // Add request header
        httpClient.setRequestProperty("Content-Type", "application/json")
        httpClient.setRequestProperty(
            "Authorization",
            "Bearer $apiKey"
        )

        // Build the body of the request
        val body = JSONObject()
        body.put("prompt", question)
        body.put("n", 1)
        body.put("size", "512x512")

        // Send the request
        httpClient.doOutput = true
        val outputStream = httpClient.outputStream
        outputStream.write(body.toString().toByteArray())
        outputStream.flush()
        outputStream.close()

        // Get the response
        return try {
            val inputStream = httpClient.inputStream
            val responseBody = inputStream.bufferedReader().readText()
            val url =
                ((JSONObject(responseBody).get("data") as JSONArray)[0] as JSONObject).get("url") as String
             "{error: \"\", url:\"${cleanText(url)}\"}"
        } catch (e: FileNotFoundException) {
            "{error: \"Cannot generate image. May violate policy.\", url:\"\"}"
        }
    }

    private fun cleanText (inText:String) :String {
        return inText.trim().replace("\"", "\\\"")
    }

    fun getDataAsync(question: String) = GlobalScope.async {
        return@async submitRequestGetData(question)
    }

    fun getImageAsync(question: String) = GlobalScope.async {
        return@async submitRequestGetImage(question)
    }
}