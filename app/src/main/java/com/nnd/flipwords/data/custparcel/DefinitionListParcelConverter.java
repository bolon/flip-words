package com.nnd.flipwords.data.custparcel;

import android.os.Parcel;

import com.nnd.flipwords.data.mw.WordDefinition;

import org.parceler.Parcels;

/**
 * Created by Android dev on 8/2/17.
 */

public class DefinitionListParcelConverter extends RealmListParcelConverter<WordDefinition> {

    @Override
    public void itemToParcel(WordDefinition input, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public WordDefinition itemFromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(WordDefinition.class.getClassLoader()));
    }
}
