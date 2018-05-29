package br.unicamp.ft.r176257.myapplication.helloCharts;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.adapter.MyAdapterLegendaGrafico;
import br.unicamp.ft.r176257.myapplication.auxiliar.Categoria;
import br.unicamp.ft.r176257.myapplication.auxiliar.Despesa;
import br.unicamp.ft.r176257.myapplication.database.DatabaseHelper;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PlaceholderDonut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static class PlaceholderFragment extends Fragment implements MyAdapterLegendaGrafico.OnItemClickListener {

        private Date dataInicio = null;
        private Date dataFim = null;

        private PieChartView chart;
        private PieChartData data;

        private boolean hasLabels = false;
        private boolean hasLabelsOutside = false;
        private boolean hasCenterCircle = false;
        private boolean hasCenterText1 = false;
        private boolean hasCenterText2 = false;
        private boolean isExploded = false;
        private boolean hasLabelForSelected = false;

        private View rootView;
        private DatabaseHelper dbHelper;
        private SQLiteDatabase sqLiteDatabase;
        private Map<Categoria, Float> despesaPorCategoria;
        private List<Categoria> categorias;
        private List<Float> porcentagemCategorias;
        private RecyclerView mRecyclerView;
        private MyAdapterLegendaGrafico mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private List<Despesa> legenda;

        private TextView txtNenhumaDespesa;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);

            rootView = inflater.inflate(R.layout.activity_grafico_donut, container, false);
            txtNenhumaDespesa = (TextView) rootView.findViewById(R.id.txt_nenhuma_despesa);

            chart = (PieChartView) rootView.findViewById(R.id.grafico_donut);
            chart.setOnValueTouchListener(new ValueTouchListener());

            hasCenterText2 = true;
            hasCenterText1 = true; // text 2 need text 1 to by also drawn.
            hasCenterCircle = true;
            toggleLabelForSelected();

            dbHelper = new DatabaseHelper(inflater.getContext());
            sqLiteDatabase = dbHelper.getReadableDatabase();

            mRecyclerView =(RecyclerView) rootView.findViewById(R.id.recycler_legenda);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            generateData();
            mAdapter = new MyAdapterLegendaGrafico(legenda, this);
            mAdapter.setActivity(getActivity());
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }

        @Override
        public void onItemClick(Despesa despesa) {

        }

        public void setDataInicio (Date data) {
            dataInicio = data;
            generateData();
        }

        public void setDataFim (Date data) {
            dataFim = data;
            generateData();
        }

        private void selectDespesaPorCategoria() {
            if ((dataInicio != null) && (dataFim != null) && (sqLiteDatabase != null)) {
                selectCategorias();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String data1 = formatter.format(dataInicio);
                String data2 = formatter.format(dataFim);
                String sql = "SELECT Categoria._id, SUM(Despesa.Valor) FROM " +
                        "Categoria JOIN Despesa ON Categoria._id = Despesa.IdCategoria " +
                        " WHERE Despesa.Data BETWEEN \"" + data1 + "\" AND \"" + data2 +
                        "\" GROUP BY Categoria._id ORDER BY Categoria._id";
                System.out.println(sql);
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                despesaPorCategoria = new LinkedHashMap<>();
                if (cursor.moveToFirst()){
                    do {
                        despesaPorCategoria.put(findCategoriaById(cursor.getInt(0)), cursor.getFloat(1));
                    } while(cursor.moveToNext());
                }
                cursor.close();
                setPorcentagemCategorias();
            }
            generateLegenda();
        }

        private Categoria findCategoriaById(int id) {
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getId() == id) {
                    return categorias.get(i);
                }
            }
            return null;
        }

        private void selectCategorias() {
            String sql = "Select Categoria._id, Categoria.Categoria, Cor.Hex from Categoria " +
                    "JOIN Cor WHERE Categoria.IdCor = Cor._id ORDER BY Categoria._id";
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            categorias = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    Categoria c = new Categoria();
                    c.setId(cursor.getInt(0));
                    c.setNome(cursor.getString(1));
                    c.setCor(cursor.getString(2));
                    categorias.add(c);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        private void setPorcentagemCategorias() {
            Float total = 0f;
            porcentagemCategorias = new ArrayList<>();
            for(Map.Entry<Categoria, Float> entry : despesaPorCategoria.entrySet()) {
                total += entry.getValue();
            }
            for (Map.Entry<Categoria, Float> entry : despesaPorCategoria.entrySet()) {
                porcentagemCategorias.add((100 * entry.getValue()) / total);
            }
        }

        private void generateLegenda() {
            legenda = new ArrayList<>();
            if ((despesaPorCategoria != null) && (!despesaPorCategoria.isEmpty())) {
                for (Map.Entry<Categoria, Float> entry : despesaPorCategoria.entrySet()) {
                    Despesa d = new Despesa();
                    d.setCategoria(entry.getKey());
                    d.setDespesa(entry.getValue());
                    legenda.add(d);
                }
            }
            if (mAdapter != null) {
                mAdapter = new MyAdapterLegendaGrafico(legenda, this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        public void generateData() {
            selectDespesaPorCategoria();
            List<SliceValue> values = new ArrayList<SliceValue>();
            if (despesaPorCategoria != null) {
                int i = 0;
                for (Map.Entry<Categoria, Float> entry : despesaPorCategoria.entrySet()) {
                    SliceValue sliceValue = new SliceValue(porcentagemCategorias.get(i), Color.parseColor(entry.getKey().getCor()));
                    values.add(sliceValue);
                    i++;
                }

                if (despesaPorCategoria.isEmpty()) {
                    txtNenhumaDespesa.setVisibility(View.VISIBLE);
                }
                else {
                    txtNenhumaDespesa.setVisibility(View.GONE);
                }
            }

            data = new PieChartData(values);
            data.setHasLabels(hasLabels);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
            data.setHasLabelsOutside(hasLabelsOutside);
            data.setHasCenterCircle(hasCenterCircle);

            if (isExploded) {
                data.setSlicesSpacing(24);
            }

            if (getActivity() != null) {
                if (hasCenterText1) {
                    data.setCenterText1("");

                    // Get roboto-italic font.
                    Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Italic.ttf");
                    data.setCenterText1Typeface(tf);

                    // Get font size from dimens.xml and convert it to sp(library uses sp values).
                    data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                            (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
                }

                if (hasCenterText2) {
                    data.setCenterText2("");

                    Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Italic.ttf");

                    data.setCenterText2Typeface(tf);
                    data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                            (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
                }
            }

            if (chart != null) {
                chart.setPieChartData(data);
            }
        }

        private void explodeChart() {
            isExploded = !isExploded;
            generateData();

        }

        private void toggleLabelsOutside() {
            // has labels have to be true:P
            hasLabelsOutside = !hasLabelsOutside;
            if (hasLabelsOutside) {
                hasLabels = true;
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }

            generateData();

        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
                hasLabelsOutside = false;

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link //Chart#startDataAnimation()}
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (SliceValue value : data.getValues()) {
                value.setTarget((float) Math.random() * 30 + 15);
            }
        }

        private class ValueTouchListener implements PieChartOnValueSelectListener {

            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                data.setCenterText1(String.format("%.0f", value.getValue()) + "%");

                List<Despesa> temp = new ArrayList<>();
                for (int i = 0; i < legenda.size(); i++) {
                    temp.add(legenda.get(i));
                }

                Collections.sort(temp, new Comparator<Despesa>() {
                    @Override public int compare(Despesa p1, Despesa p2) {
                        return p1.getCategoria().getId() - p2.getCategoria().getId(); // Ascending
                    }

                });
                System.out.println(arcIndex);
                String nomeCategoria = temp.get(arcIndex).getCategoria().getNome();

                if (nomeCategoria.equals("Outras")) {
                    nomeCategoria = getResources().getString(R.string.outras);
                }
                data.setCenterText2(nomeCategoria);
            }

            @Override
            public void onValueDeselected() {
                data.setCenterText1("");
                data.setCenterText2("");
            }

        }
    }
}