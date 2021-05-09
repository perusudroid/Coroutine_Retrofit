import com.google.gson.annotations.SerializedName

data class LoginResponse (
	@SerializedName("access_token") val access_token : String,
	@SerializedName("token_type") val token_type : String,
	@SerializedName("user") val user : User?
)