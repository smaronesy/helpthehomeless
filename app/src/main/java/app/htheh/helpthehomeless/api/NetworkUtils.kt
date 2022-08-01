package app.htheh.helpthehomeless.api

import org.json.JSONObject

fun parseWalkScoreJsonResult(jsonResult: JSONObject): Int {
    val walkscore = jsonResult.get("walkscore")
    return walkscore as Int
}

// Will be used in the next iteration
fun parseWalkScoreLogoUrl(jsonResult: JSONObject): String {
    val wsLogoUrl = jsonResult.get("logo_url")
    return wsLogoUrl as String
}