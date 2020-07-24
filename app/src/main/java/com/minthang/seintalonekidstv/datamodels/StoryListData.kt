package com.minthang.seintalonekidstv.datamodels

import android.os.Parcel
import android.os.Parcelable

data class StoryListData(val story_id:String, val thumbnail_url: String, var title: String, val summary: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(story_id)
        parcel.writeString(thumbnail_url)
        parcel.writeString(title)
        parcel.writeString(summary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoryListData> {
        override fun createFromParcel(parcel: Parcel): StoryListData {
            return StoryListData(parcel)
        }

        override fun newArray(size: Int): Array<StoryListData?> {
            return arrayOfNulls(size)
        }
    }
}