package br.unicamp.ft.r176257.myapplication;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class GraficoDonutFragment extends Fragment {

    private FiltrosGraficos filtros = new FiltrosGraficos();

    public GraficoDonutFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View lview = inflater.inflate(R.layout.grafico_donut, container, false);

        LinearLayout item = (LinearLayout) lview.findViewById(R.id.layoutInflater);
        View child = this.getActivity().getLayoutInflater().inflate(R.layout.filtros_graficos, null);
        item.addView(child);
        filtros.instanciar(savedInstanceState, child, 0, this.getActivity());

        // Inflate the layout for this fragment
        return lview;
    }
}