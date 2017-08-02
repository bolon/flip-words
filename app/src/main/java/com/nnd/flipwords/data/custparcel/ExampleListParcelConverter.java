package com.nnd.flipwords.data.custparcel;

import android.os.Parcel;

import com.nnd.flipwords.data.mw.WordExample;

import org.parceler.Parcels;

/**
 * Created by Android dev on 8/2/17.
 */

public class ExampleListParcelConverter extends RealmListParcelConverter<WordExample> {
    @Override
    public void itemToParcel(WordExample input, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public WordExample itemFromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(WordExample.class.getClassLoader()));
    }
}
