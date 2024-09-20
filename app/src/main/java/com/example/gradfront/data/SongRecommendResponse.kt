package com.example.gradfront.data

import android.os.Parcel
import android.os.Parcelable

data class SongRecommendResponse (
    val artistName: String,
    val artistID: String,
    val trackName: String,
    val trackID: String,
    val imageUrl: String,
    val previewUrl: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(artistName)
        parcel.writeString(artistID)
        parcel.writeString(trackName)
        parcel.writeString(trackID)
        parcel.writeString(imageUrl)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongRecommendResponse> {
        override fun createFromParcel(parcel: Parcel): SongRecommendResponse {
            return SongRecommendResponse(parcel)
        }

        override fun newArray(size: Int): Array<SongRecommendResponse?> {
            return arrayOfNulls(size)
        }
    }
}