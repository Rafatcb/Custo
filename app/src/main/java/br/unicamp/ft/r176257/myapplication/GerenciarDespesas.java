package br.unicamp.ft.r176257.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GerenciarDespesas extends AppCompatActivity {

    private LinearLayout parentLayout;
    private List<Integer> cores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciar_despesas);
        cores.add(Color.BLUE);
        cores.add(Color.GREEN);
        cores.add(Color.RED);
        cores.add(Color.CYAN);
        cores.add(Color.MAGENTA);
        parentLayout = (LinearLayout)findViewById(R.id.scroll_container);
    }

    public void addClick(View view) {
        EditText edtTxt = (EditText) findViewById(R.id.edttxt_despesa);

        LinearLayout linhaNova = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linhaNova.setLayoutParams(layoutParams);
        linhaNova.setOrientation(LinearLayout.HORIZONTAL);

        int tamDp = (int) getResources().getDimension(R.dimen.tamanho_view_cor);
        View colorView = new View(this);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                tamDp,
                tamDp);
        tamDp = (int) getResources().getDimension(R.dimen.margin_pequena_grude);
        viewParams.setMargins(tamDp, tamDp, tamDp, tamDp);
        colorView.setLayoutParams(viewParams);
        colorView.setBackgroundColor(cores.get(1));

        TextView textNome = new TextView(this);
        LinearLayout.LayoutParams txtNomeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2f);
        textNome.setLayoutParams(txtNomeParams);
        textNome.setGravity(Gravity.CENTER_VERTICAL);
        textNome.setText("Texto");

        TextView textData = new TextView(this);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tamDp = (int) getResources().getDimension(R.dimen.margin_media_grude);
        txtParams.setMargins(tamDp, tamDp, tamDp, tamDp);;
        textData.setLayoutParams(txtParams);
        textData.setGravity(Gravity.CENTER_VERTICAL);
        textData.setText("20/03/2018");

        TextView textDespesa = new TextView(this);
        textDespesa.setLayoutParams(txtParams);
        textDespesa.setGravity(Gravity.CENTER_VERTICAL);
        textDespesa.setText("R$ 321,20");

        edtTxt.setText("");
        linhaNova.addView(colorView);
        linhaNova.addView(textNome);
        linhaNova.addView(textData);
        linhaNova.addView(textDespesa);
        parentLayout.addView(linhaNova);
    }
}
