package com.example.e_commerce.UI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.Pojo.DummyData;
import com.example.e_commerce.Pojo.ItemClass;
import com.example.e_commerce.Pojo.MenuAdapter;
import com.example.e_commerce.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.onItemClickListener {

    RecyclerView perRec,lapRec,phoneRec;
    MenuAdapter adapter1,adapter2,adapter3;
    TextView all,perfumes,phones,laptops,tv1,tv2,tv3;
    EditText searchEt;
    final int REQUEST_CODE = 15;
    DummyData dd;
    ProgressBar pb;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();


        initComponents();
        setUpRecyclerViews();
       // checkIntent();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

/*    void checkIntent(){
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("scannerResult"))
                searchEt.setText(intent.getStringExtra("scannerResult"));
                searchItems(searchEt.getText().toString());
        }
    }*/

    void initComponents(){
        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView1);
        searchEt = findViewById(R.id.search_et);
        tv3 = findViewById(R.id.textView3);
        all = findViewById(R.id.all);
        perfumes = findViewById(R.id.perfumes);
        phones = findViewById(R.id.phones);
        laptops = findViewById(R.id.laptops);
        lapRec = findViewById(R.id.laps_rec);
        phoneRec = findViewById(R.id.phone_rec);
        perRec = findViewById(R.id.perf_rec);
        pb = findViewById(R.id.pb);
    }

    void setUpRecyclerViews(){
        adapter1 = new MenuAdapter();
        adapter2 = new MenuAdapter();
        adapter3 = new MenuAdapter();

        dd = ViewModelProviders.of(this).get(DummyData.class);

        dd.Laptops();
        dd.Perfumes();
        dd.Phones();

        dd.getPhonesLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
            @Override
            public void onChanged(ArrayList<ItemClass> itemClasses) {
                adapter1.setItems(itemClasses);
                adapter1.setListener(MenuActivity.this);
                tv1.setAlpha(255f);
                pb.setVisibility(View.GONE);
                phoneRec.setAdapter(adapter1);
                phoneRec.setLayoutManager(new LinearLayoutManager(MenuActivity.this, RecyclerView.HORIZONTAL,false));
                phoneRec.setHasFixedSize(true);
            }
        });

        dd.getLaptopsLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
            @Override
            public void onChanged(ArrayList<ItemClass> itemClasses) {
                adapter2.setItems(itemClasses);
                adapter2.setListener(MenuActivity.this);
                pb.setVisibility(View.GONE);
                tv2.setAlpha(255f);
                lapRec.setAdapter(adapter2);
                lapRec.setLayoutManager(new LinearLayoutManager(MenuActivity.this, RecyclerView.HORIZONTAL,false));
                lapRec.setHasFixedSize(true);
            }
        });

        dd.getPerfumesLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
            @Override
            public void onChanged(ArrayList<ItemClass> itemClasses) {
                adapter3.setItems(itemClasses);
                adapter3.setListener(MenuActivity.this);
                pb.setVisibility(View.GONE);
                tv3.setAlpha(255f);
                perRec.setAdapter(adapter3);
                perRec.setLayoutManager(new LinearLayoutManager(MenuActivity.this, RecyclerView.HORIZONTAL,false));
                perRec.setHasFixedSize(true);
            }
        });

    }

    public void onComponentClick(View view) {
        int id = view.getId();
        if(id == R.id.all){
            animateMenuItems(35,12,12,12);
            tv1.setText(getResources().getString(R.string.phones));
            setRecyclerData(dd.getPhonesLiveData().getValue(),RecyclerView.VISIBLE);
        }
        else if(id == R.id.perfumes){
            animateMenuItems(12,12,12,35);
            tv1.setText(getResources().getString(R.string.perfumes));
            setRecyclerData(dd.getPerfumesLiveData().getValue(),RecyclerView.GONE);
        }
        else if(id == R.id.phones){
            animateMenuItems(12,12,35,12);
            tv1.setText(getResources().getString(R.string.phones));
            setRecyclerData(dd.getPhonesLiveData().getValue(),RecyclerView.GONE);
        }
        else if(id == R.id.laptops){
            animateMenuItems(12,35,12,12);
            tv1.setText(getResources().getString(R.string.laptops));
            setRecyclerData(dd.getLaptopsLiveData().getValue(),RecyclerView.GONE);
        }
        else if(id == R.id.qr){
            Intent intent = new Intent(MenuActivity.this, ScannerActivity.class);
            startActivityForResult(intent,2);
            Animatoo.animateSlideLeft(this);
        }
        else if(id == R.id.mic){
            speak();
        }
        else if(id == R.id.exit){
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
            Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.card){
            Intent intent = new Intent(MenuActivity.this, CardActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(this);
        }
    }

    void animateMenuItems(float first,float second,float third,float fourth){
        all.animate().translationZ(first).setDuration(200);
        laptops.animate().translationZ(second).setDuration(200);
        phones.animate().translationZ(third).setDuration(200);
        perfumes.animate().translationZ(fourth).setDuration(200);
    }

    void setRecyclerData(ArrayList<ItemClass> arr,int vis){
        adapter1.setItems(arr);
        phoneRec.setAdapter(adapter1);
        lapRec.setVisibility(vis);
        perRec.setVisibility(vis);
        tv2.setVisibility(vis);
        tv3.setVisibility(vis);
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hey, Say something..");
        try{
            startActivityForResult(intent,REQUEST_CODE);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!= null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchEt.setText(result.get(0));
            searchItems(searchEt.getText().toString());
        }
        else if(requestCode == 2 && resultCode == RESULT_OK && data!= null){
            searchEt.setText(data.getStringExtra("scannerResult"));
            searchItems(searchEt.getText().toString());
        }
    }

    @Override
    public void onItemClick(ItemClass item, ImageView img) {
        Intent intent = new Intent(MenuActivity.this,ItemProfileActivity.class);
        intent.putExtra("item",(Serializable)item);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,img,"item_name");
        startActivity(intent,options.toBundle());
    }


    void searchItems(String str){
        if(str.length() > 0){
            ArrayList<ItemClass>res = new ArrayList<>();
            ArrayList<ItemClass>items = dd.getPhonesLiveData().getValue();
            for(ItemClass item : items){
                if(item.getName().toLowerCase().contains(str.toLowerCase())){
                    res.add(item);
                }
            }
            items = dd.getPerfumesLiveData().getValue();
            for(ItemClass item : items){
                if(item.getName().toLowerCase().contains(str.toLowerCase())){
                    res.add(item);
                }
            }
            items = dd.getLaptopsLiveData().getValue();
            for(ItemClass item : items){
                if(item.getName().toLowerCase().contains(str.toLowerCase())){
                    res.add(item);
                }
            }
            adapter1.setItems(res);
            adapter1.setListener(this);
            phoneRec.setAdapter(adapter1);
            perRec.setVisibility(View.GONE);
            lapRec.setVisibility(View.GONE);
            tv1.setText("Search Result");
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
        }
        else{
            perRec.setVisibility(View.VISIBLE);
            lapRec.setVisibility(View.VISIBLE);
            tv1.setText(getResources().getString(R.string.phones));
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            setUpRecyclerViews();
        }
    }


}