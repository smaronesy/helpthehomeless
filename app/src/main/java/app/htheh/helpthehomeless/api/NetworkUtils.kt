package app.htheh.helpthehomeless.api

import org.json.JSONObject

fun parseWalkScoreJsonResult(jsonResult: JSONObject): Int {
    val walkscore = jsonResult.get("walkscore")
//    val logo_url = jsonResult.get("logo_url")
//    val more_info_icon = jsonResult.get("more_info_icon")
//    val more_info_link = jsonResult.get("more_info_link")
//    val help_link = jsonResult.get("help_link")

    return walkscore as Int
}