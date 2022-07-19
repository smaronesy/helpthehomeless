package app.htheh.helpthehomeless.api

import app.htheh.helpthehomeless.utils.Constants
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface CrimeApiServiice {
    @GET ( "score?")
    fun getProperties(
        @Query("wsapikey") key: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("address") address: String,
        @Query("format") format: String,
        @Query("bike") bike: Int,
        @Query("transit") transit: Int
    ): Call<String>
}

object WalkScoreApi {
    val retrofitService: CrimeApiServiice by lazy {
        retrofit.create(CrimeApiServiice::class.java)
    }
}
