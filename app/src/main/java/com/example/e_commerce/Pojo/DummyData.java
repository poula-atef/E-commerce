package com.example.e_commerce.Pojo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DummyData extends AndroidViewModel {
    private static DatabaseReference dbr;
    private MutableLiveData<ArrayList<ItemClass>> perfumesLiveData,phonesLiveData,laptopsLiveData,cardLiveData;

    public DummyData(@NonNull Application application) {
        super(application);
    }

    private void getPerfumesData(){
        ArrayList<ItemClass> perfumes = new ArrayList<>();
        perfumesLiveData = new MutableLiveData<>();
        dbr = FirebaseDatabase.getInstance().getReference("Perfumes");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()){
                    ItemClass item = ss.getValue(ItemClass.class);
                    perfumes.add(new ItemClass(item.getImage(),item.getQuantity(),item.getPrice(),item.getName()));
                    Log.i("tag", " ================> " + item.getImage());
                }
                perfumesLiveData.setValue(perfumes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLaptopsData(){
        ArrayList<ItemClass> laptops = new ArrayList<>();
        laptopsLiveData= new MutableLiveData<>();
        dbr = FirebaseDatabase.getInstance().getReference("Laptops");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()){
                    ItemClass item = ss.getValue(ItemClass.class);
                    laptops.add(new ItemClass(item.getImage(),item.getQuantity(),item.getPrice(),item.getName()));
                    Log.i("tag", " ================> " + item.getImage());
                }
                laptopsLiveData.setValue(laptops);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPhonesData(){
        ArrayList<ItemClass> phones = new ArrayList<>();
        phonesLiveData= new MutableLiveData<>();
        dbr = FirebaseDatabase.getInstance().getReference("Phones");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()){
                    ItemClass item = ss.getValue(ItemClass.class);
                    phones.add(new ItemClass(item.getImage(),item.getQuantity(),item.getPrice(),item.getName()));
                    Log.i("tag", " ================> " + item.getImage());
                }
                phonesLiveData.setValue(phones);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCardData(){
        ArrayList<ItemClass> card = new ArrayList<>();
        cardLiveData= new MutableLiveData<>();
        dbr = FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid());
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()){
                    ItemClass item = ss.getValue(ItemClass.class);
                    card.add(new ItemClass(item.getImage(),item.getQuantity(),item.getPrice(),item.getName()));
                    Log.i("tag", " ================> " + item.getImage());
                }
                cardLiveData.setValue(card);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void Perfumes(){
        getPerfumesData();
    }

    public void Phones(){
        getPhonesData();
    }

    public void Laptops(){
        getLaptopsData();
    }

    public void Card(){
        getCardData();
    }

    public MutableLiveData<ArrayList<ItemClass>> getPerfumesLiveData() {
        return perfumesLiveData;
    }

    public MutableLiveData<ArrayList<ItemClass>> getPhonesLiveData() {
        return phonesLiveData;
    }

    public MutableLiveData<ArrayList<ItemClass>> getLaptopsLiveData() {
        return laptopsLiveData;
    }

    public MutableLiveData<ArrayList<ItemClass>> getCardLiveData() {
        return cardLiveData;
    }
}
