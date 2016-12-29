package com.esiea.arnaud.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esiea.arnaud.beerdex.R;
import com.esiea.arnaud.objects.Beer;

import java.util.ArrayList;

/**
 * Created by alabyre on 19/12/2016.
 */

public class BeerdexAdapter extends RecyclerView.Adapter<BeerdexAdapter.BeerHolder>{

    public ArrayList<Beer> beers;

    public BeerdexAdapter(ArrayList<Beer> beers){
        this.beers = beers;
    }

    @Override
    public BeerHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LinearLayout linearLayout = (LinearLayout)layoutInflater.inflate(R.layout.recycler_beer, parent, false);
        BeerHolder beerHolder = new BeerHolder(linearLayout);
        return beerHolder;
    }

    @Override
    public void onBindViewHolder(BeerHolder beerHolder, int position){
        Beer myBeer = beers.get(position);
        beerHolder.setText(myBeer.getName());
    }

    public void setNewBeer(ArrayList<Beer> beers){
        Log.d("[SetNewBeer]","SetNewBeer: "+beers.toString());
        this.beers.clear();
        this.beers.addAll(beers);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return beers.size();
    }

    public class BeerHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public BeerHolder(View v){
            super(v);
            mTextView = (TextView) v.findViewById(R.id.rv_beer_element);
        }

        public void setText(String text){
            mTextView.setText(text);
        }

    }
}
