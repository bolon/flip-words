package com.nnd.flipwords.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nnd.flipwords.FlipWordsApp;
import com.nnd.flipwords.R;
import com.nnd.flipwords.Utils;
import com.nnd.flipwords.data.APIErrorAction;
import com.nnd.flipwords.data.APIResolver;
import com.nnd.flipwords.data.WordsInterface;
import com.nnd.flipwords.data.mw.ResponseWord;
import com.nnd.flipwords.data.mw.WordDefinition;
import com.nnd.flipwords.data.mw.WordExample;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<Realm> {
    private static final String WORDS_INTENT_KEY = "words_key";
    private static final String WORDS_LIST_INTENT_KEY = "list_words_key";
    private static int TOTAL_TO_FETCH = 15;
    @Inject @Named("def") WordsInterface wordsAPI;
    @Inject @Named("rx") WordsInterface wordsAPIRX;

    @Inject Realm realm;

    @BindView(R.id.flipView) EasyFlipView flipView;
    @BindView(R.id.card_container) View cardContainerView;
    @BindView(R.id.card_back_container) View cardBackContainer;
    @BindView(R.id.animation_view) View animationView;
    @BindView(R.id.parent_layout) CoordinatorLayout parentLayout;
    @BindView(R.id.txtWord_cardfront) TextView txtViewCardFront;
    @BindView(R.id.txtWord_cardback) TextView txtViewCardBack;
    @BindView(R.id.txtType_cardback) TextView txtViewType;
    @BindView(R.id.txtMeaning_cardback) TextView txtViewMeaning;
    @BindView(R.id.txtExample_cardback) TextView txtViewExample;
    @BindView(R.id.layout_card_back) View layoutCardBack;

    Consumer<ResponseWord> onNextAction;
    Consumer<Throwable> onErrorAction;
    Action onCompleteAction;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    //fetch from api -> after done -> fetch from db -> show first -> on click -> delete 1st
    //fetch from db -> show 1st ->
    @OnClick(R.id.btnYes_cardback)
    protected void onYesBtnClicked() {
        cardBackContainer.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);

        RealmResults<ResponseWord> words = realm.where(ResponseWord.class).findAll();

        if (words.size() == 1) {
            realm.removeAllChangeListeners();
            Observable<List<String>> obsDate = Observable.fromArray(Utils.getRandomDate(TOTAL_TO_FETCH));
            obsDate.flatMapIterable(dates -> dates)
                    .flatMap(date -> wordsAPIRX.getWordOfTheDayRX(date))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new APIResolver(3, 100))
                    .subscribe(onNextAction, onErrorAction, onCompleteAction);
            return;
        }

        realm.addChangeListener(this);

        realm.executeTransaction(t -> t.where(ResponseWord.class).findFirst().deleteFromRealm());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlipWordsApp.getComponent(getApplicationContext()).inject(this);
        ButterKnife.bind(this);
        layoutCardBack.setVisibility(View.INVISIBLE);

        onNextAction = word -> realm.executeTransaction(trx -> trx.copyToRealmOrUpdate(word));
        onCompleteAction = this::onYesBtnClicked;
        onErrorAction = new APIErrorAction(parentLayout);

        ResponseWord word = realm.where(ResponseWord.class).findFirst();
        setupWordCard(word);
    }

    void setupWordCard(ResponseWord word) {
        int defIdx = Utils.randBetween(0, word.getDefinitions().size());
        int exIdx = Utils.randBetween(0, word.getExamples().size());

        WordDefinition def = word.getDefinitions().get(defIdx);
        WordExample ex = word.getExamples().get(exIdx);

        txtViewCardFront.setText(word.getWord().trim());
        txtViewCardBack.setText(word.getWord());
        txtViewType.setText(def.getPartOfSpeech());
        txtViewMeaning.setText(def.getText());
        txtViewExample.setText(ex.getText());

        animationView.setVisibility(View.INVISIBLE);
    }

    public void onClickBtnFront(View v) {
        layoutCardBack.setVisibility(View.VISIBLE);
        flipView.flipTheView();
    }

    @Override
    public void onChange(Realm t) {
        ResponseWord w = t.where(ResponseWord.class).findFirst();
        nextWord(w);
    }

    void nextWord(ResponseWord word) {
        setupWordCard(word);
        cardBackContainer.setVisibility(View.VISIBLE);
        flipView.flipTheView();
    }
}
