package com.nnd.flipwords.data.mw;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Android dev on 7/17/17.
 */

@Parcel
public class ResponseWord {
    long id;
    String category;
    String word;
    List<WordDefinition> definitions;
    List<WordExample> examples;
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

    public List<WordDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<WordDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<WordExample> getExamples() {
        return examples;
    }

    public void setExamples(List<WordExample> examples) {
        this.examples = examples;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
