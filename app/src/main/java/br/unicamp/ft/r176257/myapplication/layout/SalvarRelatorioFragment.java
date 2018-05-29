package br.unicamp.ft.r176257.myapplication.layout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.auxiliar.Relatorio;
import br.unicamp.ft.r176257.myapplication.database.DatabaseHelper;
import br.unicamp.ft.r176257.myapplication.layout.charts.FiltrosGraficos;

public class SalvarRelatorioFragment extends Fragment {

    private View lview;
    private View child;
    private FiltrosGraficos filtros = new FiltrosGraficos();
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int id;


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
            Button salvar = (Button) lview.findViewById(R.id.btnSalvarRelatorio);
            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salvarRelatorio(v);
                }
            });
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            dbHelper = new DatabaseHelper(this.getActivity());
            sqLiteDatabase = dbHelper.getReadableDatabase();

            mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot remoteRespostas : dataSnapshot.getChildren()) {
                        for (DataSnapshot remoteResposta : remoteRespostas.getChildren()) {
                            Relatorio relatorio = remoteResposta.getValue(Relatorio.class);
                            id = relatorio.getRelatorio();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }

            });
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

    public void salvarRelatorio(View botao) {
        botao.setEnabled(false);
        List<Relatorio> relatorios = selectRelatorio(filtros.getDataInicio(), filtros.getDataFim());
        if (relatorios.isEmpty()) {
            Toast.makeText(this.getContext(), getResources().getString(R.string.erro_relatorio_vazio), Toast.LENGTH_SHORT).show();
        }
        else {
            for (int i = 0; i < relatorios.size(); i++) {
                mFirebaseDatabaseReference.child("relatorios").push().setValue(relatorios.get(i));
            }
            Toast.makeText(this.getContext(), getResources().getString(R.string.relatorio_sucesso), Toast.LENGTH_SHORT).show();
        }
        botao.setEnabled(true);
    }
    private List<Relatorio> selectRelatorio(String dataInicio, String dataFim) {
        String query = "SELECT c.Categoria, SUM(d.Valor) FROM Categoria c JOIN Despesa d ON c._id = d.IdCategoria " +
                "WHERE d.data BETWEEN ? AND ? GROUP BY c.Categoria ORDER BY c._id";

        String[] strings = {dataInicio, dataFim};
        Cursor cursor = sqLiteDatabase.rawQuery(query, strings);
        List<Relatorio> relatorios = new ArrayList<>();
        int idRel = id+1;
        if (cursor.moveToFirst()){
            do {
                Relatorio relatorio = new Relatorio(idRel, cursor.getString(0), cursor.getFloat(1),
                        dataInicio, dataFim);
                relatorios.add(relatorio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return relatorios;
    }
}
