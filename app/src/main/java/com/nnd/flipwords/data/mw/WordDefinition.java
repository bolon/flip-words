package com.nnd.flipwords.data.mw;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.WordDefinitionRealmProxy;

/**
 * Created by Android dev on 7/24/17.
 */

@Parcel(implementations = WordDefinitionRealmProxy.class)
public class WordDefinition extends RealmObject {
    String text;
    String source;
    String note;
    String partOfSpeech;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
}
