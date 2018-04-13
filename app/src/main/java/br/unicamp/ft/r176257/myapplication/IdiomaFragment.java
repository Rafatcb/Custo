package br.unicamp.ft.r176257.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ricar on 03/04/2018.
 */

public class IdiomaFragment extends Fragment implements MyAdapter.OnItemClickListener {

    private View lview;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Idioma idioma;

    public IdiomaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.idioma_fragment, container, false);
        }
        mRecyclerView =(RecyclerView) lview.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Idioma> idiomas = new ArrayList<>();

        Idioma pt = new Idioma("Português", "pt", R.drawable.flag_br);
        pt.setLocale2("BR");
        idiomas.add(pt);
        idiomas.add(new Idioma("English", "en", R.drawable.flag_usa));
        idiomas.add(new Idioma("Español", "es", R.drawable.flag_es));

        mAdapter = new MyAdapter(idiomas,this);
        mAdapter.setActivity(this.getActivity());
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onItemClick(Idioma idioma) {
        this.idioma = idioma;
        Resources res = this.getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();

        if (idioma.hasLocale2()) {
            conf.setLocale(new Locale(idioma.getLocale1(), idioma.getLocale2())); // API 17+ only.
        }
        else {
            conf.setLocale(new Locale(idioma.getLocale1()));
        }
        res.updateConfiguration(conf, dm);
        this.getActivity().getIntent().putExtra("trocou_idioma", true);
        this.getActivity().recreate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
