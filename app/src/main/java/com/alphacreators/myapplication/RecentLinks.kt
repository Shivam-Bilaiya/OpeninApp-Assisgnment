package com.alphacreators.myapplication

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


 // Data class is used to hold data of recent links

data class RecentLinks(
    @SerializedName("web_link") var webLink: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("total_clicks") var totalClicks: Int? = null,
    @SerializedName("original_image") var originalImage: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(webLink)
        parcel.writeString(totalClicks.toString())
        parcel.writeString(originalImage.toString())
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentLinks> {
        override fun createFromParcel(parcel: Parcel): RecentLinks {
            return RecentLinks(parcel)
        }

        override fun newArray(size: Int): Array<RecentLinks?> {
            return arrayOfNulls(size)
        }
    }
}
