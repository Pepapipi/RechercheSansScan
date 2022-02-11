package com.example.recycler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgramAdapter adapter;

    List<Produit> produits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonFromKeyword jsonFromKeyword = new JsonFromKeyword();
        jsonFromKeyword.activity=this;
        jsonFromKeyword.execute("Pizza Delamama", "1");



    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProgramAdapter(produits);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void jsonGot(String json){
        TextView txt = findViewById(R.id.text);
        try {
             produits = Produit.getProductsListFromJson(json);
        }
        catch (Exception e){
            txt.setText(e.toString());
        }
        initRecyclerView();

    }
}