package com.example.logintest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel(
    val id: Int,
//    val password: String,
    val username: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String,
    @Json(name = "is_staff")
    val isStaff: Boolean,
//    val eventsParticipated: Int,
    val profileImageUrl: String? = null,
    val isActive: Boolean = true,
    val dateJoined: String? = null
)
/*{
    constructor(parcel: Parcel) : this(   /*questa funzione è stata creata per accountinfoscreen, per poter passare
                                            al navigation.kt un parcelable, da cancellare poi se non serve più*/
        parcel.readInt(),
        parcel.readString().toString(),     // EDO IO LA PARCELLA LA CHIEDEREI ALLO SPICHIATRA DIO CANE
        parcel.readString().toString(),     // dannato chatgpt
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(password)
        parcel.writeString(username)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeByte(if (isStaff) 1 else 0)
        parcel.writeInt(eventsParticipated)
        parcel.writeString(profileImageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}*/