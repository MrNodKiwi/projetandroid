package com.esiea.arnaud.beerdex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.esiea.arnaud.adapter.BeerdexAdapter;
import com.esiea.arnaud.objects.Beer;

import java.util.ArrayList;

public class Beerdex extends AppCompatActivity {

    private RecyclerView rv_beer;
    private BeerdexAdapter beerdexAdapter;
    private ArrayList<Beer> beers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beerdex);

        rv_beer = (RecyclerView)findViewById(R.id.rv_beer);
        rv_beer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        beers = new ArrayList<Beer>();
        beers.add(new Beer("In the futur"));
        beers.add(new Beer("this will be"));
        beers.add(new Beer("a list of beer"));
        beers.add(new Beer("that you have"));
        beers.add(new Beer("catch with the"));
        beers.add(new Beer("scanner button"));
        beers.add(new Beer("in the main menu."));
        beers.add(new Beer("This activity"));
        beers.add(new Beer("will probably"));
        beers.add(new Beer("change of design"));
        beers.add(new Beer("but I like to"));
        beers.add(new Beer("have name on the"));
        beers.add(new Beer("beer bottle."));
        beers.add(new Beer("One"));
        beers.add(new Beer("Two"));
        beers.add(new Beer("Three"));
        beers.add(new Beer("Tin Tin"));
        beers.add(new Beer("DADaaaa"));
        beers.add(new Beer("Oye"));
        beers.add(new Beer("Here you can"));
        beers.add(new Beer("SCROOOOOLLL"));
        beerdexAdapter = new BeerdexAdapter(beers);
        rv_beer.setAdapter(beerdexAdapter);
    }

}
