import android.provider.SyncStateContract
import com.example.flightmobileapp.Models.JoyStickData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>
    @POST("/api/Command")
    fun createPost(@Body data: JoyStickData): Call<ResponseBody>
}
