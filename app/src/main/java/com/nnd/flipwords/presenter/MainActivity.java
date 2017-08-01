package com.nnd.flipwords.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nnd.flipwords.FlipWordsApp;
import com.nnd.flipwords.R;
import com.nnd.flipwords.Utils;
import com.nnd.flipwords.data.WordsInterface;
import com.nnd.flipwords.data.mw.ResponseWord;
import com.nnd.flipwords.data.mw.WordDefinition;
import com.nnd.flipwords.data.mw.WordExample;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String WORDS_INTENT_KEY = "words_key";
    @Inject WordsInterface wordsAPI;

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

    public static Intent createIntent(Context context, ResponseWord word) {
        Intent intent = new Intent(context, MainActivity.class);

        if (word != null) intent.putExtra(WORDS_INTENT_KEY, Parcels.wrap(word));

        return intent;
    }

    @OnClick(R.id.btnYes_cardback)
    protected void onYesBtnClicked() {
        cardBackContainer.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);

        txtViewCardBack.setOnClickListener(view -> {

        });

        wordsAPI.getWordOfTheDay(Utils.getRandomDate()).enqueue(new Callback<ResponseWord>() {
            @Override
            public void onResponse(Call<ResponseWord> call, Response<ResponseWord> response) {
                if (response.body() != null) {
                    flipView.flipTheView();
                    setupWordCard(response.body());
                    cardBackContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseWord> call, Throwable t) {
                cardBackContainer.setVisibility(View.INVISIBLE);
                Snackbar.make(parentLayout, "Check your connection", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlipWordsApp.getComponent().inject(this);
        ButterKnife.bind(this);
        layoutCardBack.setVisibility(View.INVISIBLE);

        if (getIntent().getParcelableExtra(WORDS_INTENT_KEY) != null) {
            ResponseWord word = Parcels.unwrap(getIntent().getParcelableExtra(WORDS_INTENT_KEY));
            setupWordCard(word);
        }
    }

    void setupWordCard(ResponseWord word) {
        int defIdx = Utils.randBetween(0, word.getDefinitions().size());
        int exIdx = Utils.randBetween(0, word.getExamples().size());

        WordDefinition def = word.getDefinitions().get(defIdx);
        WordExample ex = word.getExamples().get(exIdx);

        txtViewCardFront.setText(word.getWord());
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
}
