package io.ipoli.android.app.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.ipoli.android.Constants;
import io.ipoli.android.R;
import io.ipoli.android.app.activities.QuickAddActivity;
import io.ipoli.android.app.utils.ViewUtils;
import io.ipoli.android.challenge.activities.EditChallengeActivity;
import io.ipoli.android.quest.activities.EditQuestActivity;
import io.ipoli.android.reward.activities.EditRewardActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 6/27/16.
 */
public class FabMenuView extends RelativeLayout {
    private Unbinder unbinder;

    @BindView(R.id.fab_menu_container)
    ViewGroup container;

    @BindView(R.id.fab_add_quest)
    FloatingActionButton quest;

    @BindView(R.id.fab_add_repeating_quest)
    FloatingActionButton repeatingQuest;

    @BindView(R.id.fab_add_challenge)
    FloatingActionButton challenge;

    @BindView(R.id.fab_add_reward)
    FloatingActionButton reward;

    @BindView(R.id.fab_quick_add_quest)
    FloatingActionButton quickQuest;

    @BindView(R.id.fab_label)
    TextView fabLabel;

    @BindView(R.id.fab1_label)
    TextView fab1Label;

    @BindView(R.id.fab2_label)
    TextView fab2Label;

    @BindView(R.id.fab3_label)
    TextView fab3Label;

    @BindView(R.id.fab4_label)
    TextView fab4Label;

    private Animation fabOpen;
    private Animation fabClose;
    private Animation rotateForward;
    private Animation rotateBackward;
    private boolean isOpen = false;

    public FabMenuView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initUI(context);
        }
    }

    public FabMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initUI(context);
        }
    }

    private void initUI(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_fab_menu, this);
        unbinder = ButterKnife.bind(this, view);

        setElevation(ViewUtils.dpToPx(5, getResources()));

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

    }

    @OnClick(R.id.fab_add_quest)
    public void onAddQuestClick(View view) {
        fabClick(EditQuestActivity.class);
    }

    @OnClick(R.id.fab_add_repeating_quest)
    public void onAddRepeatingQuestClick(View view) {
        fabClick(EditQuestActivity.class);
    }

    @OnClick(R.id.fab_add_challenge)
    public void onAddChallengeClick(View view) {
        fabClick(EditChallengeActivity.class);
    }

    @OnClick(R.id.fab_add_reward)
    public void onAddRewardClick(View view) {
        fabClick(EditRewardActivity.class);
    }

    @OnClick(R.id.fab_quick_add_quest)
    public void onQuickAddQuestClick(View view) {
        if(isOpen) {
            Intent addIntent = new Intent(getContext(), QuickAddActivity.class);
            addIntent.putExtra(Constants.QUICK_ADD_ADDITIONAL_TEXT, " " + getContext().getString(R.string.today).toLowerCase());
            getContext().startActivity(addIntent);
            close();
        } else {
            open();
        }
    }

    private void fabClick(Class<?> clazz) {
        if(isOpen) {
            getContext().startActivity(new Intent(getContext(), clazz));
            close();
        } else {
            open();
        }
    }

    public void open() {
        isOpen = true;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        container.setClickable(true);
        container.setVisibility(VISIBLE);
        container.setAlpha(1);
        playOpenAnimation();
    }

    private void close() {
        isOpen = false;
        container.setClickable(false);
        playCloseAnimation();
    }

    private void playOpenAnimation() {
        rotateForward.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                quest.setImageResource(R.drawable.ic_done_white_24dp);
                quest.setRotation(-45);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        quest.startAnimation(rotateForward);
        fabLabel.startAnimation(fabOpen);
        repeatingQuest.startAnimation(fabOpen);
        fab1Label.startAnimation(fabOpen);
        challenge.startAnimation(fabOpen);
        fab2Label.startAnimation(fabOpen);
        reward.startAnimation(fabOpen);
        fab3Label.startAnimation(fabOpen);
        quickQuest.startAnimation(fabOpen);
        fab4Label.startAnimation(fabOpen);
    }


    private void playCloseAnimation() {
        rotateBackward.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                quest.setImageResource(R.drawable.ic_add_white_24dp);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fabClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.animate().alpha(0).setDuration(getResources().getInteger(
                        android.R.integer.config_shortAnimTime)).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        quest.startAnimation(rotateBackward);
        quickQuest.startAnimation(fabClose);
        fab4Label.startAnimation(fabClose);
        reward.startAnimation(fabClose);
        fab3Label.startAnimation(fabClose);
        fab2Label.startAnimation(fabClose);
        challenge.startAnimation(fabClose);
        fab1Label.startAnimation(fabClose);
        repeatingQuest.startAnimation(fabClose);
        fabLabel.startAnimation(fabClose);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick(R.id.fab_menu_container)
    public void onContainerClick(View v) {
        close();
    }

    @Override
    protected void onDetachedFromWindow() {
        unbinder.unbind();
        super.onDetachedFromWindow();
    }
}
