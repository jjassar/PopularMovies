package com.jagdeep.myapps.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by jagdeep on 14/07/15.
 */
public class MyParcelable implements Parcelable {

    String js;

    public MyParcelable(String j)
    {
        js = j;

    }

    private MyParcelable(Parcel in)
    {
        js = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(js);
    }


    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>()
    {

        @Override
        public MyParcelable createFromParcel(Parcel parcel) {
            return new MyParcelable(parcel);
        }

        @Override
        public MyParcelable[] newArray(int i) {
            return new MyParcelable[i];
        }
    };

}
