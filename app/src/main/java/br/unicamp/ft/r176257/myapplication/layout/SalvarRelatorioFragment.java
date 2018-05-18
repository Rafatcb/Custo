package br.unicamp.ft.r176257.myapplication.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.layout.charts.FiltrosGraficos;

public class SalvarRelatorioFragment extends Fragment {

    private View lview;
    private View child;
    private FiltrosGraficos filtros = new FiltrosGraficos();

    public SalvarRelatorioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.activity_salvar_relatorio, container, false);
            child = this.getActivity().getLayoutInflater().inflate(R.layout.filtros_graficos, null);
            LinearLayout item = (LinearLayout) lview.findViewById(R.id.layoutInflater);
            item.addView(child);
            filtros.instanciar(child, 2, this.getActivity());
        }
        filtros.updateGraficos();
        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
