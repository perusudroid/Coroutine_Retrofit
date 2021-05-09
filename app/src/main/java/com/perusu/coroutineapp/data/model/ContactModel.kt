package com.perusu.coroutineapp.data.model

import com.google.gson.annotations.SerializedName


data class ContactModel(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("photo")
    var photo: String? = null,
    @SerializedName("thumbNail")
    var thumbNail: String? = null,
    @SerializedName("phone")
    var phone: MutableSet<String?>? = null,
    @SerializedName("email")
    public var emails: MutableSet<String?>? = null,
    @SerializedName("inVisibleGroup")
    var inVisibleGroup: Int = 0,
    @SerializedName("starred")
    var starred: Boolean? = null,
) : Comparable<ContactModel> {
    override fun compareTo(other: ContactModel): Int {
        return if (name != null && other.name != null) {
            name!!.compareTo(other.name!!)
        } else -1

    }
}