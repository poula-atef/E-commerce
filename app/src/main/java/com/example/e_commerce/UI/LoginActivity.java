package com.example.e_commerce.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.R;
import com.example.e_commerce.Pojo.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.blurry.Blurry;

public class LoginActivity extends AppCompatActivity {

    ImageView img;
    Button btn,switchBtn,resetBtn,resetBackBtn;
    EditText user,password,repassword,email,resetEt;
    TextView tv,reset,date;
    LinearLayout layout;
    CheckBox remember;
    int colorFrom,colorTo,top,left,right,bottom;
    boolean log=true;
    LinearLayout.LayoutParams params;
    AlphaAnimation alphaAnimation;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseUser fUser;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkRememberMe();
        initComponents();
        Blurry.with(this).sampling(2).from(BitmapFactory.decodeResource(getResources(),R.drawable.shop_2)).into(img);
        initConstants();
        componentsAnimation();

    }

    public void onComponentClick(View view){
        int id = view.getId();
        if(id == R.id.log_switch){
            startBackAnimation();
            startAccessButtonAnimation();
            startSwitchButtonAnimation();
            changeComonents();
            swapColors();
            changeLogValue();
        }
        else if(id == R.id.reset){
            goToReset();
        }
        else if(id == R.id.reset_back_btn){
            backToLogin();
        }
        else if(id == R.id.btn){
            if(log && !TextUtils.isEmpty(user.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){
                auth.signInWithEmailAndPassword(user.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    if(remember.isChecked()){
                                        editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                        editor.putString("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        editor.putString("email",email.getText().toString());
                                        editor.apply();
                                    }
                                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                                    startActivity(intent);
                                    Animatoo.animateSlideLeft(LoginActivity.this);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(!log && !TextUtils.isEmpty(user.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) &&
                    !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(repassword.getText().toString()) && !date.getText().toString().equals(getResources().getString(R.string.birth_date))){
                if(password.getText().toString().equals(repassword.getText().toString())){
                    auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                                        ref = FirebaseDatabase.getInstance().getReference("Users");
                                        ref.child(fUser.getUid()).setValue(new UserClass(user.getText().toString(),email.getText().toString(),password.getText().toString(),date.getText().toString()));
                                        Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                                        startActivity(intent);
                                        Animatoo.animateSlideLeft(LoginActivity.this);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Password Fields Must Be The Same..", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Please, Fill All Fields!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(id == R.id.reset_btn){
            if(TextUtils.isEmpty(resetEt.getText().toString())){
                Toast.makeText(this, "Write Your Email..", Toast.LENGTH_SHORT).show();
            }
            else{
                auth.sendPasswordResetEmail(resetEt.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Reset Email Has Sent To Your Email..", Toast.LENGTH_SHORT).show();
                                    resetEt.getText().clear();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(id == R.id.date){
            Intent intent = new Intent(LoginActivity.this, CalenderActivity.class);
            startActivityForResult(intent,5);
            Animatoo.animateSlideRight(this);
        }
    }
    void checkRememberMe(){
        if(!PreferenceManager.getDefaultSharedPreferences(this).getString("userId","?").equals("?")){
            Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
    void startBackAnimation(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleX",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleX",0f,1f);
        animator1.setDuration(500);
        animator2.setDuration(500);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator2.start();
            }
        });
        animator1.start();
    }
    void startSwitchButtonAnimation(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.log_switch)),"scaleY",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.log_switch)),"scaleY",0f,1f);
        animator1.setDuration(500);
        animator2.setDuration(500);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((Button)findViewById(R.id.log_switch)).setTextColor(colorTo);
                ((Button)findViewById(R.id.log_switch)).setBackgroundTintList(ColorStateList.valueOf(colorFrom));
                if(log) {
                    ((Button) findViewById(R.id.log_switch)).setText("Log In");
                }
                else{
                    ((Button) findViewById(R.id.log_switch)).setText("Sign Up");
                }
            animator2.start();
            }
        });
        animator1.start();
    }
    void startAccessButtonAnimation(){
        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorTo,colorFrom);
        ValueAnimator backColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorFrom,colorTo);
        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                btn.setTextColor((int) animation.getAnimatedValue());
            }
        });
        backColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                btn.setBackgroundTintList(ColorStateList.valueOf((int) animation.getAnimatedValue()));
            }
        });
        textColorAnimator.setDuration(1000);
        backColorAnimator.setDuration(1000);
        btn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.button_rotate));
        backColorAnimator.start();
        textColorAnimator.start();
    }
    void swapColors(){
        int tmp = colorFrom;
        colorFrom = colorTo;
        colorTo = tmp;
    }
    void changeComonents(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(log){
                            int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                            params.setMargins(left,top,right,bottom);
                            tv.setText(getResources().getString(R.string.create_account_title));
                            tv.setLayoutParams(params);
                            user.setHint("User Name...");
                            reset.setVisibility(View.GONE);
                            remember.setVisibility(View.GONE);
                            date.setVisibility(View.VISIBLE);
                            repassword.setVisibility(View.VISIBLE);
                            email.setVisibility(View.VISIBLE);
                        }
                        else{
                            int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                            params.setMargins(left,top,right,bottom);
                            tv.setLayoutParams(params);
                            tv.setText(getResources().getString(R.string.have_account_title));
                            user.setHint("User Name or Email...");
                            reset.setVisibility(View.VISIBLE);
                            remember.setVisibility(View.VISIBLE);
                            repassword.setVisibility(View.GONE);
                            date.setVisibility(View.GONE);
                            email.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 500);
    }
    void changeLogValue(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(log)
                    log = false;
                else
                    log = true;
            }
        }, 550);
    }
    void initComponents(){
        img = findViewById(R.id.img);
        resetBtn = findViewById(R.id.reset_btn);
        resetBackBtn = findViewById(R.id.reset_back_btn);
        resetEt = findViewById(R.id.reset_et);
        switchBtn = findViewById(R.id.log_switch);
        btn = findViewById(R.id.btn);
        email = findViewById(R.id.email);
        repassword = findViewById(R.id.rewrite_password);
        tv = findViewById(R.id.welcome_txt);
        user = findViewById(R.id.username);
        password = findViewById(R.id.password);
        layout = findViewById(R.id.linearLayout3);
        reset = findViewById(R.id.reset);
        remember = findViewById(R.id.remember);
        date = findViewById(R.id.date);
    }
    void initConstants(){
        colorTo = getResources().getColor(R.color.purple_500);
        colorFrom = Color.rgb(255,255,255);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        right = left;
        alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        auth = FirebaseAuth.getInstance();
    }
    void componentsAnimation(){
        img.animate().alpha(1.0f).setDuration(500).setListener(null);
        ((View)findViewById(R.id.black_back)).animate().alpha(0.7f).setDuration(500).setStartDelay(700).setListener(null);
        btn.animate().alpha(1.0f).setDuration(500).setStartDelay(1500).setListener(null);
        layout.animate().alpha(1.0f).setDuration(500).setStartDelay(1500).setListener(null);
        switchBtn.animate().alpha(1.0f).setDuration(500).setStartDelay(1500).setListener(null);
    }
    void goToReset(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleY",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleY",0f,1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.log_switch)),"scaleY",1f,0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(((Object)btn),"scaleY",1f,0f);
        animator1.setDuration(500);
        animator2.setDuration(500);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                user.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                remember.setVisibility(View.GONE);
                tv.setText(getResources().getString(R.string.reset_title));
                reset.setVisibility(View.GONE);
                resetBtn.setVisibility(View.VISIBLE);
                resetBackBtn.setVisibility(View.VISIBLE);
                resetEt.setVisibility(View.VISIBLE);
                animator2.start();
                animator3.start();
                animator4.start();
            }
        });
        animator1.start();
    }
    void backToLogin(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleY",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.linearLayout3)),"scaleY",0f,1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(((Object)findViewById(R.id.log_switch)),"scaleY",0f,1f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(((Object)btn),"scaleY",0f,1f);
        animator1.setDuration(500);
        animator2.setDuration(500);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                user.setVisibility(View.VISIBLE);
                remember.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                tv.setText(getResources().getString(R.string.have_account_title));
                reset.setVisibility(View.VISIBLE);
                resetBtn.setVisibility(View.GONE);
                resetBackBtn.setVisibility(View.GONE);
                resetEt.setVisibility(View.GONE);
                animator2.start();
                animator3.start();
                animator4.start();
            }
        });
        animator1.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 5 && resultCode == 5){
            date.setText(data.getStringExtra("date"));
        }
    }
}