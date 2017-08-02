package com.nnd.flipwords.data.mw;

import com.nnd.flipwords.data.custparcel.DefinitionListParcelConverter;
import com.nnd.flipwords.data.custparcel.ExampleListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelProperty;
import org.parceler.ParcelPropertyConverter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.ResponseWordRealmProxy;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Android dev on 7/17/17.
 */

@Parcel(implementations = {ResponseWordRealmProxy.class}, value = Parcel.Serialization.BEAN, analyze = {ResponseWord.class})
public class ResponseWord extends RealmObject {
    @PrimaryKey long id;
    String category;
    String word;
    RealmList<WordDefinition> definitions;
    RealmList<WordExample> examples;
    String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public RealmList<WordDefinition> getDefinitions() {
        return definitions;
    }

    @ParcelProperty("definitions")
    @ParcelPropertyConverter(DefinitionListParcelConverter.class)
    public void setDefinitions(RealmList<WordDefinition> definitions) {
        this.definitions = definitions;
    }

    public RealmList<WordExample> getExamples() {
        return examples;
    }

    @ParcelProperty("examples")
    @ParcelPropertyConverter(ExampleListParcelConverter.class)
    public void setExamples(RealmList<WordExample> examples) {
        this.examples = examples;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
