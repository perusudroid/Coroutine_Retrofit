import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class User (

	@SerializedName("email") val email : String,
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("phone") val phone : Int,
	@SerializedName("total_storage") val total_storage : String,
	@SerializedName("is_admin") val is_admin : String,
	@SerializedName("is_superadmin") val is_superadmin : Boolean,
	@SerializedName("address") val address : String,
	@SerializedName("location") val location : String,
	@SerializedName("file_name") val file_name : String,
	@SerializedName("company") val company : String,
	@SerializedName("is_active") val is_active : Boolean,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("sub_start_date") val sub_start_date : String,
	@SerializedName("sub_end_date") val sub_end_date : String,
	@SerializedName("plan_type") val plan_type : Int,
	@SerializedName("url") val url : String
)