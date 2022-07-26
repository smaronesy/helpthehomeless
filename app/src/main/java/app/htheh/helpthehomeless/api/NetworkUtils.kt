package app.htheh.helpthehomeless.api

import org.json.JSONObject

fun parseWalkScoreJsonResult(jsonResult: JSONObject): Int {

    val walkscore = jsonResult.get("walkscore")
    return walkscore as Int

}