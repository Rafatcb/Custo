package br.unicamp.ft.r176257.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GraficoLinhasFragment extends Fragment {

    private View lview;
    private View child;
    private FiltrosGraficos filtros = new FiltrosGraficos();

    public GraficoLinhasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.grafico_linhas, container, false);
            child = this.getActivity().getLayoutInflater().inflate(R.layout.filtros_graficos, null);
            LinearLayout item = (LinearLayout) lview.findViewById(R.id.layoutInflater);
            item.addView(child);
            filtros.instanciar(savedInstanceState, child, 1, this.getActivity());
        }

        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
