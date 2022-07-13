package app.htheh.helpthehomeless.utils

class States {

    var STATE_MAP: MutableMap<String, String> = mutableMapOf()

    init {
        STATE_MAP["Alabama"] = "AL"
        STATE_MAP["Alaska"] = "AK"
        STATE_MAP["Alberta"] = "AB"
        STATE_MAP["Arizona"] = "AZ"
        STATE_MAP["Arkansas"] = "AR"
        STATE_MAP["British Columbia"] = "BC"
        STATE_MAP["California"] = "CA"
        STATE_MAP["Colorado"] = "CO"
        STATE_MAP["Connecticut"] = "CT"
        STATE_MAP["Delaware"] = "DE"
        STATE_MAP["District Of Columbia"] = "DC"
        STATE_MAP["Florida"] = "FL"
        STATE_MAP["Georgia"] = "GA"
        STATE_MAP["Guam"] = "GU"
        STATE_MAP["Hawaii"] = "HI"
        STATE_MAP["Idaho"] = "ID"
        STATE_MAP["Illinois"] = "IL"
        STATE_MAP["Indiana"] = "IN"
        STATE_MAP["Iowa"] = "IA"
        STATE_MAP["Kansas"] = "KS"
        STATE_MAP["Kentucky"] = "KY"
        STATE_MAP["Louisiana"] = "LA"
        STATE_MAP["Maine"] = "ME"
        STATE_MAP["Manitoba"] = "MB"
        STATE_MAP["Maryland"] = "MD"
        STATE_MAP["Massachusetts"] = "MA"
        STATE_MAP["Michigan"] = "MI"
        STATE_MAP["Minnesota"] = "MN"
        STATE_MAP["Mississippi"] = "MS"
        STATE_MAP["Missouri"] = "MO"
        STATE_MAP["Montana"] = "MT"
        STATE_MAP["Nebraska"] = "NE"
        STATE_MAP["Nevada"] = "NV"
        STATE_MAP["New Brunswick"] = "NB"
        STATE_MAP["New Hampshire"] = "NH"
        STATE_MAP["New Jersey"] = "NJ"
        STATE_MAP["New Mexico"] = "NM"
        STATE_MAP["New York"] = "NY"
        STATE_MAP["Newfoundland"] = "NF"
        STATE_MAP["North Carolina"] = "NC"
        STATE_MAP["North Dakota"] = "ND"
        STATE_MAP["Northwest Territories"] = "NT"
        STATE_MAP["Nova Scotia"] = "NS"
        STATE_MAP["Nunavut"] = "NU"
        STATE_MAP["Ohio"] = "OH"
        STATE_MAP["Oklahoma"] = "OK"
        STATE_MAP["Ontario"] = "ON"
        STATE_MAP["Oregon"] = "OR"
        STATE_MAP["Pennsylvania"] = "PA"
        STATE_MAP["Prince Edward Island"] = "PE"
        STATE_MAP["Puerto Rico"] = "PR"
        STATE_MAP["Quebec"] = "QC"
        STATE_MAP["Rhode Island"] = "RI"
        STATE_MAP["Saskatchewan"] = "SK"
        STATE_MAP["South Carolina"] = "SC"
        STATE_MAP["South Dakota"] = "SD"
        STATE_MAP["Tennessee"] = "TN"
        STATE_MAP["Texas"] = "TX"
        STATE_MAP["Utah"] = "UT"
        STATE_MAP["Vermont"] = "VT"
        STATE_MAP["Virgin Islands"] = "VI"
        STATE_MAP["Virginia"] = "VA"
        STATE_MAP["Washington"] = "WA"
        STATE_MAP["West Virginia"] = "WV"
        STATE_MAP["Wisconsin"] = "WI"
        STATE_MAP["Wyoming"] = "WY"
        STATE_MAP["Yukon Territory"] = "YT"
    }

    fun getStateAbbreviation(state: String): String? {
        if (STATE_MAP!!.containsKey(state)) {
            return STATE_MAP!![state]
        } else {
            return state
        }
    }
}