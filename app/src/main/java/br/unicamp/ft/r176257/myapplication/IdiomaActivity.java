package br.unicamp.ft.r176257.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by ricar on 03/04/2018.
 */

public class IdiomaActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiomas);

        mRecyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Idioma> idiomas = new ArrayList<>();

        idiomas.add(new Idioma("Português", R.drawable.flag_br));
        idiomas.add(new Idioma("Inglês", R.drawable.flag_usa));
        idiomas.add(new Idioma("Espanhol", R.drawable.flag_es));

        mAdapter = new MyAdapter(idiomas,this);
        mAdapter.setActivity(this);
        mRecyclerView.setAdapter(mAdapter);
    }
    public Idioma idioma;
    @Override
    public void onItemClick(Idioma idioma) {
        this.idioma = idioma;

    }


}
