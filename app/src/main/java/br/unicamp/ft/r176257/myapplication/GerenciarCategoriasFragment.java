package br.unicamp.ft.r176257.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GerenciarCategoriasFragment extends Fragment {

    private View lview;
    static private int MAX_CATEGORIAS = 5;
    private int qtdCategorias = 0;
    private List<Integer> cores = new ArrayList<>();
    private Stack<Integer> categoriasLivres = new Stack<>();
    private List<Integer> categoriasOcupadas = new ArrayList<>();
    private LinearLayout parentLayout;

    public GerenciarCategoriasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.gerenciar_categorias, container, false);
            parentLayout = (LinearLayout) lview.findViewById(R.id.scroll_container);
            lview.findViewWithTag("btn_add_categoria0").setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addClick(view);
                        }
                    }
            );


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
        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void addClick(View view) {
        qtdCategorias++;

        LinearLayout layoutCatAdicionada = (LinearLayout)parentLayout.findViewWithTag("layout" + categoriasOcupadas.get(categoriasOcupadas.size()-1));


        int tamBtn = (int) getResources().getDimension(R.dimen.btn_add_width);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                tamBtn,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button btnEdit = (Button) layoutCatAdicionada.findViewWithTag("btn_add_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        btnEdit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
        btnEdit.setTag("btn_edit_categoria" + categoriasOcupadas.get(categoriasOcupadas.size()-1));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria
                editClick(v);
            }
        });

        Button btnDel = new Button(this.getActivity());
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
        LinearLayout subLayout = new LinearLayout(this.getActivity());
        subLayout.setTag("layout" + categoriasLivres.peek());
        subLayout.setOrientation(LinearLayout.HORIZONTAL);

        DisplayMetrics metrics = this.getActivity().getApplicationContext().getResources().getDisplayMetrics();
        int tamView =  (int) getResources().getDimension(R.dimen.tamanho_view_cor);

        View colorView = new View(this.getActivity());
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                tamView,
                tamView);
        int tamDp = (int) getResources().getDimension(R.dimen.margin_pequena_grude);
        viewParams.setMargins(tamDp, tamDp, tamDp, tamDp);
        colorView.setLayoutParams(viewParams);
        colorView.setTag("color" + categoriasLivres.peek());
        colorView.setBackgroundColor(cores.get(categoriasLivres.peek()));

        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams (
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        edtParams.setMargins(0,tamDp,0,tamDp);
        EditText edtTxt = new EditText(this.getActivity());
        edtTxt.setHint(R.string.categoria);
        edtTxt.setLayoutParams(edtParams);
        edtTxt.setTag("txt_categoria" + categoriasLivres.peek());

        Button btnAdd = new Button(this.getActivity());
        btnAdd.setTag("btn_add_categoria" + categoriasLivres.peek());
        System.out.println("set tag: " + btnAdd.getTag());
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Adicionar Categoria
                addClick(v);
            }
        });

        int tamBtn = (int) getResources().getDimension(R.dimen.btn_add_width);
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
