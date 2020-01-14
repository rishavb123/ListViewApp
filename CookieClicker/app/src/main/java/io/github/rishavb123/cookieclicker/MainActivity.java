package io.github.rishavb123.cookieclicker;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private double score;
    private double rate;

    private double poisonPrice;

    private TextView scoreView;
    private ImageView clickMe;

    private ConstraintLayout constraintLayout;

    private AnimationSet animationSet;

    private int attackingItemID;
    private ImageView attackItem;
    private ImageView attackingItem;
    private int[] attackingItems;
    private double[] attackingItemPrices;
    private double[] attackingDamages;
    private double[] origAttackingDamages;
    private TextView attackingItemPrice;
    private boolean canBuyAttack;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private ImageView poison;
    private int poisonsOwned;
    private boolean canBuyPoison;
    private boolean first;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canBuyPoison = false;
        canBuyAttack = false;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        poisonsOwned = prefs.getInt("poisonsOwned", 0);
        for(int j = 0; j < poisonsOwned; j++)
        {
            ConstraintLayout l = findViewById(R.id.container);
            ImageView i = new ImageView(getApplicationContext());
            i.setId(View.generateViewId());
            i.setImageResource(R.drawable.poison);
            l.addView(i);
            ConstraintSet cs = new ConstraintSet();
            cs.clone(l);
            cs.connect(i.getId(), ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT, (j%5)*(i.getDrawable().getIntrinsicWidth() + 4));
            cs.connect(i.getId(), ConstraintSet.TOP, R.id.container, ConstraintSet.TOP, (j/5)*10);
            cs.applyTo(l);
        }

        poisonPrice = prefs.getFloat("poisonPrice", 100);
        ((TextView)findViewById(R.id.poison_price)).setText("Price: "+Math.round(poisonPrice*10)/10.0);

        poison = findViewById(R.id.poison);
        poison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate+=0.1;
                rate*=1.5;
                addToScore(-poisonPrice);
                ConstraintLayout l = findViewById(R.id.container);
                ImageView i = new ImageView(getApplicationContext());
                i.setId(View.generateViewId());
                i.setImageResource(R.drawable.poison);
                l.addView(i);
                ConstraintSet cs = new ConstraintSet();
                cs.clone(l);
                cs.connect(i.getId(), ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT, (poisonsOwned%5)*(i.getDrawable().getIntrinsicWidth() + 4));
                cs.connect(i.getId(), ConstraintSet.TOP, R.id.container, ConstraintSet.TOP, (poisonsOwned/5)*10);
                cs.applyTo(l);
                poisonPrice*=2;
                poisonsOwned++;
                ((TextView)findViewById(R.id.poison_price)).setText("Price: "+Math.round(poisonPrice*10)/10.0);
            }
        });

        attackingItems = new int[]{0, R.drawable.punch, R.drawable.fist, R.drawable.fire, R.drawable.lightning, R.drawable.hammer, R.drawable.chuck, R.drawable.attack};
        attackingItemPrices = new double[]{0, 500, 1000, 2000, 5000, 10000, 100000, 1000000000};
        attackingItemID = prefs.getInt("attackingItemID", 0);
        origAttackingDamages = new double[]{1, 2, 5, 10, 50, 100, 1000, 1000000 };
        attackingDamages = new double[]{1, 2, 5, 10, 50, 100, 1000, 1000000 };
        attackingItemPrice = findViewById(R.id.attack_price);

        attackingItem = findViewById(R.id.attackingItem);
        if(attackingItemID + 1 < attackingItems.length) {
            attackingItem.setImageResource(attackingItems[attackingItemID + 1]);
            attackingItemPrice.setText("Price: "+attackingItemPrices[attackingItemID + 1]);
        }
        attackingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attackingItemID + 1 < attackingItems.length) {
                    attackingItemID++;
                    if(attackingItemID + 1 < attackingItems.length) {
                        attackingItem.setImageResource(attackingItems[attackingItemID + 1]);
                        attackingItemPrice.setText("Price: "+attackingItemPrices[attackingItemID + 1]);
                    }
                }
                addToScore(-attackingItemPrices[attackingItemID]);
            }
        });

        constraintLayout = findViewById(R.id.layout);
        scoreView = findViewById(R.id.score_view);
        clickMe = findViewById(R.id.click_me);
        scoreView.setText(Double.toString(score));

        first = true;

        rate = prefs.getFloat("rate", 0);
        score = 0;
        addToScore(prefs.getFloat("score", 0));

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate = 0;
                poisonPrice = 100;
                poisonsOwned = 0;
                attackingItemID = 0;
                for(int i = 0; i< attackingDamages.length; i++)
                    attackingDamages[i] = origAttackingDamages[i];
                attackingItem.setImageResource(R.drawable.punch);
                attackingItemPrice.setText("Price: "+attackingItemPrices[attackingItemID + 1]);
                ConstraintLayout l = findViewById(R.id.container);
                new ConstraintSet().applyTo(l);
                l.removeAllViews();
                ((TextView)findViewById(R.id.poison_price)).setText("Price: "+Math.round(poisonPrice*10)/10.0);
                addToScore(-score);
            }
        });

        findViewById(R.id.dev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poisonPrice = 0;
                for(int i = 0; i < attackingDamages.length; i++)
                    attackingDamages[i] = 10*origAttackingDamages[i];
                ((TextView)findViewById(R.id.poison_price)).setText("Price: "+Math.round(poisonPrice*10)/10.0);
            }
        });

        findViewById(R.id.clear_poison).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToScore(Math.pow(poisonsOwned, 4)*10);
                poisonsOwned = 0;
                rate = 0;
                ConstraintLayout l = findViewById(R.id.container);
                new ConstraintSet().applyTo(l);
                l.removeAllViews();
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                add();
            }
        }, 0, 100);

        ScaleAnimation animation = new ScaleAnimation(1f,1.1f,1f,1.1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(100);
        ScaleAnimation animation2 = new ScaleAnimation(1.1f,1f,1.1f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation2.setDuration(100);
        animation2.setStartOffset(animation.getDuration());

        animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation2);

        clickMe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    clickMe.setColorFilter(Color.argb(150,30+(int)(225*((double)attackingItemID/attackingItems.length)),(int)((poisonsOwned/100.0)*255),0));

                    final TextView textView = new TextView(getApplicationContext());
                    textView.setId(View.generateViewId());
                    textView.setText("Ow!");
                    textView.setTextSize(30);
                    textView.setTextColor(Color.WHITE);

                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(layoutParams);

                    textView.measure(0, 0);

                    constraintLayout.addView(textView);

                    final ConstraintSet constraintSet = new ConstraintSet();

                    if(attackingItemID != 0)
                    {
                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setImageResource(attackingItems[attackingItemID]);
                        imageView.setId(View.generateViewId());
                        constraintLayout.addView(imageView);
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, clickMe.getId(), ConstraintSet.LEFT, (int) event.getX() - imageView.getDrawable().getIntrinsicWidth() / 2);
                        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, clickMe.getId(), ConstraintSet.TOP, (int) event.getY() - imageView.getDrawable().getIntrinsicHeight() / 2);
                        attackItem = imageView;

                    }
                    else {
                        constraintSet.clone(constraintLayout);
                    }


                    constraintSet.connect(textView.getId(), ConstraintSet.LEFT, clickMe.getId(), ConstraintSet.LEFT, (int) event.getX() - textView.getMeasuredWidth() / 2);
                    constraintSet.connect(textView.getId(), ConstraintSet.TOP, clickMe.getId(), ConstraintSet.TOP, (int) event.getY() - textView.getMeasuredHeight() / 2);

                    constraintSet.applyTo(constraintLayout);

                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -400);
                    translateAnimation.setDuration(1000);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(1000);

                    AnimationSet moveAndFade = new AnimationSet(true);
                    moveAndFade.addAnimation(translateAnimation);
                    moveAndFade.addAnimation(alphaAnimation);

                    moveAndFade.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            constraintLayout.removeView(textView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    textView.setAnimation(moveAndFade);



                    addToScore(attackingDamages[attackingItemID]);
                    v.startAnimation(animationSet);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    constraintLayout.removeView(attackItem);
                    clickMe.setColorFilter(null);
                }
                return true;
            }

        });

    }

    @Override
    protected void onPause() {
        editor.putFloat("rate", (float) rate);
        editor.putFloat("score", (float) score);
        editor.putFloat("poisonPrice", (float) poisonPrice);
        editor.putInt("attackingItemID", attackingItemID);
        editor.putInt("poisonsOwned", poisonsOwned);
        editor.commit();
        super.onPause();
    }

    public synchronized void add()
    {
        scoreView.post(new Runnable() {
            @Override
            public void run() {
                addToScore(rate);
            }
        });
    }

    public synchronized void addToScore(double x)
    {
        String s = Double.toString(Math.round((score+=x)*100.0)/100.0);
        if(s.split("\\.").length > 1 && s.split("\\.")[1].length() < 2)
            s+="0";
        scoreView.setText(s);
        if(score >= poisonPrice)
        {
            if(!canBuyPoison || first) {
                poison.setEnabled(true);
                poison.setColorFilter(null);
                canBuyPoison = true;
                AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.4, 1);
                alphaAnimation.setDuration(750);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        poison.setAlpha((float) 1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                poison.startAnimation(alphaAnimation);
            }
        }
        else {
            if(canBuyPoison || first) {
                poison.setEnabled(false);
                poison.setColorFilter(Color.argb(150, 200, 200, 200));
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, (float) 0.4);
                alphaAnimation.setDuration(750);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        poison.setAlpha((float) 0.4);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                poison.startAnimation(alphaAnimation);
                canBuyPoison = false;
            }
        }

        int buyIndex = (attackingItemID + 1 >= attackingItemPrices.length)? attackingItemPrices.length - 1: attackingItemID + 1;

        if(score >= attackingItemPrices[buyIndex])
        {
            if(!canBuyAttack || first)
            {
                attackingItem.setEnabled(true);
                attackingItem.setColorFilter(null);
                AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.4, 1);
                alphaAnimation.setDuration(750);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        attackingItem.setAlpha((float) 1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                attackingItem.startAnimation(alphaAnimation);
                canBuyAttack = true;
            }
        }
        else {
            if(canBuyAttack || first) {
                attackingItem.setEnabled(false);
                attackingItem.setColorFilter(Color.argb(150, 200, 200, 200));
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, (float) 0.4);
                alphaAnimation.setDuration(750);
                canBuyAttack = false;
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        attackingItem.setAlpha((float) 0.4);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                attackingItem.startAnimation(alphaAnimation);
            }
            first = false;
        }
    }
}