package org.avmedia.openaiandroid

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object OpenAIConnection {
    private fun submitRequestGetData(question:String):String {
        val url = URL("https://api.openai.com/v1/completions")
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.requestMethod = "POST"

        // Add request header
        httpClient.setRequestProperty("Content-Type", "application/json")
        httpClient.setRequestProperty("Authorization", "Bearer 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")

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
        val responseCode = httpClient.responseCode
        val responseMessage = httpClient.responseMessage
        val inputStream = httpClient.inputStream
        val responseBody = inputStream.bufferedReader().readText()
        return ((JSONObject(responseBody).get("choices") as JSONArray)[0] as JSONObject).get("text") as String
    }

    fun getDataAsync(question:String) = GlobalScope.async {
        return@async submitRequestGetData(question)
    }

}