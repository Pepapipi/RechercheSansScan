package com.example.recycler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgramAdapter adapter;
    JsonFromKeyword jsonFromKeyword;
    List<Produit> produits;
    RecyclerViewClickListner recyclerViewClickListner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnClickListner();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProgramAdapter(produits,recyclerViewClickListner);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void rechercheDuProduit(String s)
    {
        jsonFromKeyword = new JsonFromKeyword();
        jsonFromKeyword.activity=this;
        jsonFromKeyword.execute(s);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recherche_sans_scan,menu);
        MenuItem item = menu.findItem(R.id.search_product);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

                rechercheDuProduit(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setOnClickListner(){
        recyclerViewClickListner = new RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                int i=0;
                String text1="";
                String text2="";
                String text3="";
                while(i<produits.get(position).emball.length && text1.isEmpty())
                {
                    text1 = adapter.verificationNomDeEmballage(position,i,text1);
                    i++;
                }
                while(i<produits.get(position).emball.length && text2.isEmpty())
                {
                    text2 = adapter.verificationNomDeEmballage(position,i,text2);
                    i++;
                }
                while(i<produits.get(position).emball.length && text3.isEmpty())
                {
                    text3 = adapter.verificationNomDeEmballage(position,i,text3);
                    i++;
                }


                Intent intent = new Intent(getApplicationContext(),ProduitDetails.class);
                intent.putExtra("nomPdt", produits.get(position).getNom());
                intent.putExtra("marquePdt", produits.get(position).getMarque());
                intent.putExtra("codeBarre", produits.get(position).getCode());
                intent.putExtra("text1",text1);
                intent.putExtra("text2",text2);
                intent.putExtra("text3", text3);
                startActivity(intent);
            }
        };
    }



}