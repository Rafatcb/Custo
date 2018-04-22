package br.unicamp.ft.r176257.myapplication.layout.charts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.helloCharts.PlaceholderDonut;
import br.unicamp.ft.r176257.myapplication.helloCharts.PlaceholderLinhas;

public class FiltrosGraficos {

    private Calendar calendarInicio = Calendar.getInstance();
    private Calendar calendarFim = Calendar.getInstance();
    private EditText edttxtDataInicio;
    private EditText edttxtDataFim;
    private View filtro;
    private Activity activity;
    private Button btn30Dias;
    private Button btnMes;
    private Button btnAno;
    private Button btnSempre;
    private int tipoGrafico; // 0 = Donut, 1 = Linhas
    private PlaceholderDonut.PlaceholderFragment graficoDonut;
    private PlaceholderLinhas.PlaceholderFragment graficoLinhas;

    public void instanciar(Bundle savedInstanceState, View cont, int tipo, Activity act) {
        filtro = cont;
        activity = act;
        edttxtDataInicio = (EditText) filtro.findViewById(R.id.edttxt_data_inicio);
        edttxtDataFim = (EditText) filtro.findViewById(R.id.edttxt_data_fim);
        btn30Dias = (Button) filtro.findViewById(R.id.btnFiltro30Dias);
        btnMes = (Button) filtro.findViewById(R.id.btnFiltroMes);
        btnAno = (Button) filtro.findViewById(R.id.btnFiltroAno);
        btnSempre = (Button) filtro.findViewById(R.id.btnFiltroSempre);
        addOnClickBtnFiltros();
        tipoGrafico = tipo;
        //if (savedInstanceState == null) {
            switch (tipo) {
                case 0: // Donut
                    if (graficoDonut == null) {
                        graficoDonut = new PlaceholderDonut.PlaceholderFragment();
                        activity.getFragmentManager().beginTransaction().add(R.id.container, graficoDonut).commit();
                    }
                    break;
                case 1: // Linhas
                    if (graficoLinhas == null) {
                        graficoLinhas = new PlaceholderLinhas.PlaceholderFragment();
                        activity.getFragmentManager().beginTransaction().add(R.id.container, graficoLinhas).commit();
                    }
                    break;
            }
        //}


        final DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarInicio.set(Calendar.YEAR, year);
                calendarInicio.set(Calendar.MONTH, monthOfYear);
                calendarInicio.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDataInicio();
            }

        };

        final DatePickerDialog.OnDateSetListener dateFim = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarFim.set(Calendar.YEAR, year);
                calendarFim.set(Calendar.MONTH, monthOfYear);
                calendarFim.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDataFim();
            }

        };

        edttxtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(activity, dateInicio, calendarInicio
                        .get(Calendar.YEAR), calendarInicio.get(Calendar.MONTH),
                        calendarFim.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMaxDate(calendarFim.getTimeInMillis());
                dp.show();
            }
        });

        edttxtDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(activity, dateFim, calendarFim
                        .get(Calendar.YEAR), calendarFim.get(Calendar.MONTH),
                        calendarFim.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dp.show();
            }
        });

        updateDataFim();
        calendarInicio.set(Calendar.DAY_OF_MONTH, 1);
        updateDataInicio();
    }

    private void addOnClickBtnFiltros() {
        btn30Dias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarInicio = Calendar.getInstance();
                calendarInicio.add(Calendar.DATE, -30);
                calendarFim = Calendar.getInstance();
                updateDataInicio();
                updateDataFim();
            }
        });
        btnMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarInicio = Calendar.getInstance();
                calendarInicio.set(Calendar.DAY_OF_MONTH, 1);
                calendarFim = Calendar.getInstance();
                updateDataInicio();
                updateDataFim();
            }
        });
        btnAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarInicio = Calendar.getInstance();
                calendarInicio.set(Calendar.DAY_OF_YEAR, 1);
                calendarFim = Calendar.getInstance();
                updateDataInicio();
                updateDataFim();
            }
        });
        btnSempre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void updateDataInicio() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttxtDataInicio.setText(sdf.format(calendarInicio.getTime()));
        switch (tipoGrafico) {
            case 0: // Donut
                graficoDonut.setDataInicio(calendarInicio.getTime());
                break;
            case 1: // Linhas
                graficoLinhas.setDataInicio(calendarInicio.getTime());
                break;
        }
    }

    private void updateDataFim() {
        if (calendarFim.before(calendarInicio)) {
            calendarInicio = (Calendar) calendarFim.clone();
            updateDataInicio();
        }
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttxtDataFim.setText(sdf.format(calendarFim.getTime()));
        switch (tipoGrafico) {
            case 0: // Donut
                graficoDonut.setDataFim(calendarFim.getTime());
                break;
            case 1: // Linhas
                graficoLinhas.setDataFim(calendarFim.getTime());
                break;
        }
    }
}
