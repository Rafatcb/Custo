package br.unicamp.ft.r176257.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GerenciarCategorias extends AppCompatActivity {

    private int qtdCategorias = 0;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciar_categorias);
        parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
    }

    public void addClick(View view) {
        qtdCategorias++;

        LinearLayout layoutCatAdicionada = (LinearLayout)parentLayout.findViewWithTag("layout" + qtdCategorias);

        LinearLayout subLayout = new LinearLayout(this);
        subLayout.setTag("layout" + (qtdCategorias+1));
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams (
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        edtParams.setMargins(0,10,0,10);
        EditText edtTxt = new EditText(this);
        edtTxt.setHint(R.string.categoria);
        edtTxt.setLayoutParams(edtParams);
        edtTxt.setTag("txt_categoria" + (qtdCategorias+1));


        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float dp = 55f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

        Button btnDel = new Button(this);
        btnDel.setTag("btn_del_categoria" + qtdCategorias);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                pixels,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnDel.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria

            }
        });
        btnDel.setLayoutParams(btnParams);

        edtTxt.requestFocus();
        subLayout.addView(edtTxt);
        parentLayout.addView(subLayout);
        layoutCatAdicionada.addView(btnDel);
    }
}
