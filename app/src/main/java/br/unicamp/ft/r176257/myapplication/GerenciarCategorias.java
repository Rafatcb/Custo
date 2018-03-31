package br.unicamp.ft.r176257.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GerenciarCategorias extends AppCompatActivity {
    static private int MAX_CATEGORIAS = 5;
    private int qtdCategorias = 0;
    private List<Integer> cores = new ArrayList<>();
    private Stack<Integer> categoriasLivres = new Stack<>();
    private List<Integer> categoriasOcupadas = new ArrayList<>();
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciar_categorias);

        parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        cores.add(Color.BLUE);
        cores.add(Color.GREEN);
        cores.add(Color.RED);
        cores.add(Color.CYAN);
        cores.add(Color.MAGENTA);
        categoriasLivres.add(4);
        categoriasLivres.add(3);
        categoriasLivres.add(2);
        categoriasLivres.add(1);
        categoriasOcupadas.add(0);
        View view1 = (View) parentLayout.findViewWithTag("color0");
        view1.setBackgroundColor(cores.get(0));
    }

    public void addClick(View view) {
        qtdCategorias++;

        LinearLayout layoutCatAdicionada = (LinearLayout)parentLayout.findViewWithTag("layout" + categoriasOcupadas.get(categoriasOcupadas.size()-1));


        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * 55f; // 55f = tamanho em dp
        int tamBtn = (int) (fpixels + 0.5f);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                tamBtn,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        System.out.println("find: btn_add_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        Button btnEdit = (Button) layoutCatAdicionada.findViewWithTag("btn_add_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        btnEdit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
        btnEdit.setTag("btn_edit_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria
                editClick(v);
            }
        });

        Button btnDel = new Button(this);
        btnDel.setTag("btn_del_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        btnDel.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria
                delClick(v);
            }
        });
        btnDel.setLayoutParams(btnParams);

        layoutCatAdicionada.addView(btnDel); // Categoria que foi adicionada agora

        if (qtdCategorias < MAX_CATEGORIAS) {
            criarLinhaLayout();
        }
    }

    public void delClick(View view) {
        qtdCategorias--;
        String tag = (String) view.getTag();

        int numCategoriaExcluida = Integer.parseInt(tag.substring(tag.length() - 1));
        LinearLayout layoutCatExcluida = (LinearLayout) parentLayout.findViewWithTag("layout" + numCategoriaExcluida);
        System.out.println("excluido: " + numCategoriaExcluida);
        parentLayout.removeView(layoutCatExcluida);
        categoriasLivres.push(numCategoriaExcluida);
        categoriasOcupadas.remove(new Integer(numCategoriaExcluida));

        if (qtdCategorias == MAX_CATEGORIAS-1) {
            criarLinhaLayout();
        }
    }

    public void editClick(View view) {

    }

    public void criarLinhaLayout() {
        LinearLayout subLayout = new LinearLayout(this);
        subLayout.setTag("layout" + categoriasLivres.peek());
        subLayout.setOrientation(LinearLayout.HORIZONTAL);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * 40f; // 40f = tamanho em dp
        int tamView = (int) (fpixels + 0.5f);

        View colorView = new View(this);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                tamView,
                tamView);
        fpixels = metrics.density * 5f; // 5f = tamanho em dp
        int tamDp = (int) (fpixels + 0.5f);
        viewParams.setMargins(tamDp, tamDp, tamDp, tamDp);
        colorView.setLayoutParams(viewParams);
        colorView.setTag("color" + categoriasLivres.peek());
        colorView.setBackgroundColor(cores.get(categoriasLivres.peek()));

        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams (
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        edtParams.setMargins(0,tamDp,0,tamDp);
        EditText edtTxt = new EditText(this);
        edtTxt.setHint(R.string.categoria);
        edtTxt.setLayoutParams(edtParams);
        edtTxt.setTag("txt_categoria" + categoriasLivres.peek());

        Button btnAdd = new Button(this);
        btnAdd.setTag("btn_add_categoria" + categoriasLivres.peek());
        System.out.println("set tag: " + btnAdd.getTag());
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Adicionar Categoria
                addClick(v);
            }
        });

        fpixels = metrics.density * 55f; // 55f = tamanho em dp
        int tamBtn = (int) (fpixels + 0.5f);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                tamBtn,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnAdd.setLayoutParams(btnParams);

        edtTxt.requestFocus();
        subLayout.addView(colorView);        // O novo view de cor
        subLayout.addView(edtTxt);           // O novo campo de categoria
        subLayout.addView(btnAdd);           // O novo botão de adicionar categoria
        parentLayout.addView(subLayout);     // Add o novo layout no LinearLayout principal da Activity
        categoriasOcupadas.add(categoriasLivres.pop());
    }
}
