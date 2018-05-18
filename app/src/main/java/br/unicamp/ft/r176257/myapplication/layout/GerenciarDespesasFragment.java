package br.unicamp.ft.r176257.myapplication.layout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.adapter.MyAdapterDespesas;
import br.unicamp.ft.r176257.myapplication.auxiliar.Categoria;
import br.unicamp.ft.r176257.myapplication.auxiliar.Despesa;
import br.unicamp.ft.r176257.myapplication.database.DatabaseHelper;
import br.unicamp.ft.r176257.myapplication.dialog.ExcluirDespesaDialogFragment;

public class GerenciarDespesasFragment extends Fragment implements MyAdapterDespesas.OnItemClickListener {

    private static final int REQUEST_CODE = 1;
    private static final int MAX_ROWS = 15;
    private View lview;
    private View viewCor;
    private LinearLayout parentLayout;
    private Spinner spinner;
    private int posicaoSelecionada;
    private List<Categoria> categorias = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private List<Despesa> despesas;
    private RecyclerView mRecyclerView;
    private MyAdapterDespesas mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Despesa despesa;

    public GerenciarDespesasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lview == null) {
            lview = inflater.inflate(R.layout.gerenciar_despesas, container, false);
            parentLayout = (LinearLayout) lview.findViewById(R.id.scroll_container);
            spinner = (Spinner) lview.findViewById(R.id.spinner_categorias);
            viewCor = (View) lview.findViewById(R.id.view_cor);

            dbHelper = new DatabaseHelper(lview.getContext());
            sqLiteDatabase = dbHelper.getReadableDatabase();


            lview.findViewWithTag("btn_add").setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addClick(view);
                        }
                    }
            );
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    posicaoSelecionada = pos;
                    viewCor.setBackgroundColor(Color.parseColor(categorias.get(pos).getCor()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        popularSpinner();
        mRecyclerView =(RecyclerView) lview.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        selectDespesas();
        mAdapter = new MyAdapterDespesas(despesas, this);
        mAdapter.setActivity(this.getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return lview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onItemClick(Despesa despesa) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogExcluir = new ExcluirDespesaDialogFragment();
        dialogExcluir.setTargetFragment(this, REQUEST_CODE);
        dialogExcluir.show(fm,"dialog_excluir");
        this.despesa = despesa;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // Voltou da dialogFragment
        // Make sure fragment codes match up
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Boolean excluiu = data.getBooleanExtra("excluir", false);

            if (excluiu) {
                deleteDespesa(this.despesa);
                despesas.remove(despesa);
                Toast.makeText(getActivity(), R.string.despesa_excluida, Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addClick(View view) {
        EditText edtTxt = (EditText) this.getActivity().findViewById(R.id.edttxt_despesa);
        if ((edtTxt.getText().toString().equals("")) || (edtTxt.getText().toString().equals(null))) {
            Toast.makeText(this.getContext(), R.string.informe_valor, Toast.LENGTH_SHORT).show();
            return;
        }
        Despesa d = new Despesa();
        d.setData(new Date());
        d.setCategoria(categorias.get(posicaoSelecionada));
        d.setDespesa(Float.parseFloat(edtTxt.getText().toString()));
        insertDespesa(d);
        despesas.add(0, d);
        if (despesas.size() > MAX_ROWS) {
            despesas.remove(MAX_ROWS);
        }
        mAdapter.notifyItemInserted(0);
        mAdapter.acrescentarSelectedPos();
        edtTxt.setText("");
        Toast.makeText(this.getContext(), R.string.despesa_cadastrada, Toast.LENGTH_SHORT).show();
    }

    public void selectCategorias() {
        String sql = "Select Categoria._id, Categoria.Categoria, Cor.Hex from Categoria JOIN Cor ON Cor._id = Categoria.IdCor";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        categorias = new ArrayList<>();
        if (cursor.moveToFirst()){
            Categoria c1 = new Categoria();
            c1.setId(cursor.getInt(0));
            c1.setNome(cursor.getString(1));
            c1.setCor(cursor.getString(2));
            while(cursor.moveToNext()) {
                Categoria c = new Categoria();
                c.setId(cursor.getInt(0));
                c.setNome(cursor.getString(1));
                c.setCor(cursor.getString(2));
                categorias.add(c);
            };
            categorias.add(c1);
        }
        cursor.close();
    }

    public void popularSpinner() {
        selectCategorias();
        viewCor.setBackgroundColor(Color.parseColor(categorias.get(0).getCor()));
        List<String> categoriasNome = new ArrayList<>();
        for (int i = 0; i < categorias.size(); i++) {
            categoriasNome.add(categorias.get(i).getNome());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                lview.getContext(), android.R.layout.simple_spinner_item, categoriasNome);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void selectDespesas() {
        String[] colunas = new String[]{"_id", "Data", "Valor", "IdCategoria"};
        Cursor cursor = sqLiteDatabase.query("Despesa", colunas, null, null,
                null, null,  "_id DESC", "15");
        despesas = new ArrayList<>();
        if (cursor.moveToFirst()){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            do {
                Despesa d = new Despesa();
                d.setId(cursor.getInt(0));
                try {
                    d.setData(formatter.parse(cursor.getString(1)));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                d.setDespesa(cursor.getFloat(2));
                d.setCategoria(categorias.get(posCategorias(cursor.getInt(3)))); // Categoria "Outras"
                despesas.add(d);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    public int posCategorias(int posicao) {
        for (int i = 0; i < categorias.size() ; i++) {
            if (categorias.get(i).getId() == posicao) {
                return i;
            }
        }
        return 0;
    }

    public void insertDespesa (Despesa d) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Valor", d.getDespesa());
        contentValues.put("IdCategoria", d.getCategoria().getId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(d.getData());
        contentValues.put("Data", format);
        d.setId(selectUltimaDespesaId());
        sqLiteDatabase.insert("Despesa", null, contentValues);
    }

    public int selectUltimaDespesaId() {
        int id = 0;
        String[] colunas = new String[]{"_id"};
        Cursor cursor = sqLiteDatabase.query("Despesa", colunas, null, null,
                null, null,  "_id DESC", "1");
        if (cursor.moveToFirst()){
            id  = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public void deleteDespesa(Despesa d) {
        sqLiteDatabase.delete("Despesa", "_id=" + d.getId(), null);
    }
}
