package br.unicamp.ft.r176257.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GerenciarCategoriasFragment extends Fragment {

    private View lview;
    static private int MAX_CATEGORIAS = 6;
    private int qtdCategorias = 0;
    private List<String> cores = new ArrayList<>();
    private List<Categoria> categorias;
    private Stack<Integer> coresLivres;
    private List<Integer> coresOcupadas;
    private LinearLayout parentLayout;
    DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    public GerenciarCategoriasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.gerenciar_categorias, container, false);
            parentLayout = (LinearLayout) lview.findViewById(R.id.scroll_container);

            dbHelper = new DatabaseHelper(lview.getContext());
            sqLiteDatabase = dbHelper.getReadableDatabase();
            selectCores();
            selectCategorias();
            criarLayoutCategoriasExistentes();
            if (qtdCategorias < MAX_CATEGORIAS) {
                criarLayoutCategoriaNova();
            }
        }
        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void addClick(View v) {
        String tag = (String) v.getTag();

        tag = tag.substring(tag.length() - 1);
        EditText edt = (EditText) parentLayout.findViewWithTag("txt_categoria" + tag);
        if ((edt.getText().toString().equals("")) || (edt.getText().toString().equals(null))) {
            Toast.makeText(this.getContext(), "Informe o nome da categoria!", Toast.LENGTH_SHORT).show();
            return;
        }

        qtdCategorias++;
        int id = Integer.parseInt(tag);
        Categoria c = new Categoria();
        c.setCor(cores.get(id));
        c.setNome(((EditText) lview.findViewWithTag("txt_categoria" + id)).getText().toString());
        insertCategoria(c);
        c.setId(selectLastId());
        System.out.println(c.getId());
        try {
            categorias.set(id, c);
        } catch (IndexOutOfBoundsException ex) {
            categorias.add(c);
        }

        LinearLayout layoutCatAdicionada = (LinearLayout)parentLayout.findViewWithTag("layout" + id);


        int tamBtn = (int) getResources().getDimension(R.dimen.btn_add_width);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                tamBtn,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button btnEdit = (Button) v;
        btnEdit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
        btnEdit.setTag("btn_edit_categoria" + id);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria
                editClick(v);
            }
        });

        Button btnDel = new Button(this.getActivity());
        btnDel.setTag("btn_del_categoria" + id);
        btnDel.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // Evento do Botão Deletar Categoria
                delClick(v);
            }
        });
        btnDel.setLayoutParams(btnParams);

        layoutCatAdicionada.addView(btnDel); // Categoria que foi adicionada agora

        if (qtdCategorias < MAX_CATEGORIAS) {
            criarLayoutCategoriaNova();
        }
        Toast.makeText(this.getContext(), "Categoria cadastrada", Toast.LENGTH_SHORT).show();
    }

    public void delClick(View view) {
        qtdCategorias--;
        String tag = (String) view.getTag();

        int numCategoriaExcluida = Integer.parseInt(tag.substring(tag.length() - 1));
        int idCategorias = selectByCor(numCategoriaExcluida);
        deleteCategoria(categorias.get(idCategorias));
        LinearLayout layoutCatExcluida = (LinearLayout) parentLayout.findViewWithTag("layout" + numCategoriaExcluida);
        parentLayout.removeView(layoutCatExcluida);
        coresLivres.push(numCategoriaExcluida);
        coresOcupadas.remove(new Integer(numCategoriaExcluida));

        if (qtdCategorias == MAX_CATEGORIAS-1) {
            criarLayoutCategoriaNova();
        }
        Toast.makeText(this.getContext(), "Categoria excluída", Toast.LENGTH_SHORT).show();
    }

    public void editClick(View view) {
        String tag = (String) view.getTag();
        int id = Integer.parseInt(tag.substring(tag.length() - 1));

        int idCategorias = selectByCor(id);
        categorias.get(idCategorias).setNome(((EditText) parentLayout.findViewWithTag("txt_categoria" + id)).getText().toString());
        updateCategoria(categorias.get(idCategorias));
        Toast.makeText(this.getContext(), "Nome alterado", Toast.LENGTH_SHORT).show();
    }

    public void criarLayoutCategoriaNova() {
        int id = coresLivres.peek();

        LinearLayout subLayout = new LinearLayout(this.getActivity());
        subLayout.setTag("layout" + id);
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
        colorView.setTag("color" + id);
        colorView.setBackgroundColor(Color.parseColor(cores.get(coresLivres.peek())));

        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams (
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        edtParams.setMargins(0,tamDp,0,tamDp);
        EditText edtTxt = new EditText(this.getActivity());
        edtTxt.setHint(R.string.categoria);
        edtTxt.setLayoutParams(edtParams);
        edtTxt.setTag("txt_categoria" + id);

        Button btnAdd = new Button(this.getActivity());
        btnAdd.setTag("btn_add_categoria" + id);
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

        coresOcupadas.add(coresLivres.pop());
    }

    public void criarLayoutCategoriasExistentes() {
        for (int i = 0; i < coresOcupadas.size(); i++) {
            qtdCategorias++;
            int id = coresOcupadas.get(i);

            LinearLayout subLayout = new LinearLayout(this.getActivity());
            subLayout.setTag("layout" + id);
            subLayout.setOrientation(LinearLayout.HORIZONTAL);

            DisplayMetrics metrics = this.getActivity().getApplicationContext().getResources().getDisplayMetrics();
            int tamView = (int) getResources().getDimension(R.dimen.tamanho_view_cor);

            View colorView = new View(this.getActivity());
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    tamView,
                    tamView);
            int tamDp = (int) getResources().getDimension(R.dimen.margin_pequena_grude);
            viewParams.setMargins(tamDp, tamDp, tamDp, tamDp);
            colorView.setLayoutParams(viewParams);
            colorView.setTag("color" + id);
            colorView.setBackgroundColor(Color.parseColor(cores.get(coresOcupadas.get(i))));

            if (i > 0) {
                LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                edtParams.setMargins(0, tamDp, 0, tamDp);
                EditText edtTxt = new EditText(this.getActivity());
                edtTxt.setHint(R.string.categoria);
                edtTxt.setLayoutParams(edtParams);
                edtTxt.setTag("txt_categoria" + id);
                edtTxt.setText(categorias.get(i).getNome());


                int tamBtn = (int) getResources().getDimension(R.dimen.btn_add_width);
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                        tamBtn,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                Button btnEdit = new Button(this.getActivity());
                btnEdit.setTag("btn_edit_categoria" + id);
                btnEdit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) { // Evento do Botão Deletar Categoria
                        editClick(v);
                    }
                });
                btnEdit.setLayoutParams(btnParams);

                Button btnDel = new Button(this.getActivity());
                btnDel.setTag("btn_del_categoria" + id);
                btnDel.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
                btnDel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) { // Evento do Botão Deletar Categoria
                        delClick(v);
                    }
                });
                btnDel.setLayoutParams(btnParams);

                subLayout.addView(colorView);
                subLayout.addView(edtTxt);
                subLayout.addView(btnEdit);
                subLayout.addView(btnDel);
            }
            else {
                LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                txtParams.setMargins(0, tamDp, 0, tamDp);
                TextView txtView = new TextView(this.getActivity());
                txtView.setHeight(tamView);
                txtView.setTextSize(getResources().getDimension(R.dimen.fonte_categoria_outras));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                txtView.setText(getString(R.string.outras));
                txtView.setLayoutParams(txtParams);
                txtView.setTag("txtview_categoria" + id);

                subLayout.addView(colorView);
                subLayout.addView(txtView);
            }
            parentLayout.addView(subLayout);
        }
    }

    public void selectCores(){
        String sql = "Select Hex from Cor";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                cores.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void selectCategorias() {
        String sql = "Select _id, Categoria, IdCor from Categoria";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        categorias = new ArrayList<>();
        coresOcupadas = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Categoria c = new Categoria();
                c.setId(cursor.getInt(0));
                c.setNome(cursor.getString(1));
                c.setCor(cores.get(cursor.getInt(2)-1));  // Precisa do -1 porque no Banco o id começa em 1
                                                    // E na List o id começa em 0
                categorias.add(c);
                coresOcupadas.add(cursor.getInt(2)-1);
            }while(cursor.moveToNext());
            selectCoresLivres();
        }
        cursor.close();
    }

    public void selectCoresLivres() {
        coresLivres = new Stack<>();

        for (int i = cores.size()-1; i >= 0; i--) {
            if (!coresOcupadas.contains(i)) {
                coresLivres.push(i);
            }
        }
    }

    public void insertCategoria(Categoria c) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Categoria", c.getNome());
        contentValues.put("IdCor", (cores.indexOf(c.getCor()) + 1)); // +1 porque a List inicia em 0 e o Banco em 1

        sqLiteDatabase.insert("Categoria", null, contentValues);
    }

    public void updateCategoria(Categoria c) {
        ContentValues cv = new ContentValues();
        cv.put("Categoria", c.getNome());
        sqLiteDatabase.update("Categoria", cv, "_id=" + c.getId(), null);
    }

    public void deleteCategoria(Categoria c) {
        sqLiteDatabase.delete("Categoria", "_id=" + c.getId(), null);
    }

    public int selectLastId() {
        String sql = "SELECT _id FROM Categoria ORDER BY _id DESC LIMIT 1";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int id = 0;
        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(0);
            }while(cursor.moveToNext());
            selectCoresLivres();
        }
        cursor.close();
        return id;
    }

    public int selectByCor(int cor) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getCor().equals(cores.get(cor))) {
                System.out.println("selectByCor = " + (i));
                return i;
            }
        }
        return 0;
    }
}
