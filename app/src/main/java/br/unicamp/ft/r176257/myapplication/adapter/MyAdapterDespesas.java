package br.unicamp.ft.r176257.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import br.unicamp.ft.r176257.myapplication.R;
import br.unicamp.ft.r176257.myapplication.auxiliar.Despesa;

public class MyAdapterDespesas extends RecyclerView.Adapter<MyAdapterDespesas.ViewHolder> {

    /*
     * Variável para poder selecionar uma das linhas da listas.
     */
    private int selectedPos = RecyclerView.NO_POSITION;
    private Activity activity;

    public void acrescentarSelectedPos() {
        selectedPos++;
    }

    public void setActivity(Activity act) {
        activity = act;
    }

    /*
        Essa interface serve apenas para que possamos nos comunicar com
        a activity que está em execução. Essa activity deverá ter o método
        onItemClick.
     */
    public interface OnItemClickListener {
        void onItemClick(Despesa despesa);
    }

    private final List<Despesa> despesas;
    private final OnItemClickListener listener;

    public MyAdapterDespesas(List<Despesa> despesas, OnItemClickListener listener) {
        this.despesas = despesas;
        this.listener = listener;
    }

    /*
       Este método cria um novo ViewHolder. Será chamada apenas algumas vezes,
       dependendo de quantas linhas cabem na RecyclerView.
     */
    @Override
    public MyAdapterDespesas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
           Usamos o LayoutInflater para transformar um arquivo XML em uma classe
           java. No caso, estamos o arquivo adapter_layout.xml
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_despesa, parent, false);
        return new MyAdapterDespesas.ViewHolder(v);
    }


    /*
       Este método é chamado sempre que uma mudança ocorre na RecyclerView e
       itens precisam ser substituídos.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*
            Colocamos em verde os itens que foram selecionados. Posso deixar para o final
         */
        holder.itemView.setBackgroundColor(selectedPos == position ? Color.rgb(213, 227, 237) : Color.TRANSPARENT);
        holder.itemView.setSelected(selectedPos == position);
        holder.bind(despesas.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return despesas.size();
    }

    /*
        Esta classe fornece uma referência para as views dos itens
        que estão na RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        /*
           Como atributo, colocamos todas as views que estão no layout
           de cada linha da RecyclerView. No nosso caso, são os elementos
           que declaramos em adapter_layout.xml
         */
        private View corView;
        private TextView categoria;
        private TextView valor;
        private TextView data;


        public ViewHolder(View itemView) {
            super(itemView);
            /*
                Populamos os atributos invocando o findViewById dessa linha
                da RecyclerView.
             */
            categoria = (TextView) itemView.findViewById(R.id.categoria);
            corView = (View) itemView.findViewById(R.id.colorView);
            valor = (TextView) itemView.findViewById(R.id.valor);
            data = (TextView) itemView.findViewById(R.id.data);
        }

        /*
            Invocado quando acoplamos um objeto do array ao Holder. Note
            que os Holders são reaproveitados, então espera-se que este método
            seja chamado várias vezes.
         */
        public void bind(final Despesa despesa, final OnItemClickListener listener) {
            try {
                if (despesa.getCategoria().getNome().equals("Outras")) {
                    categoria.setText(activity.getResources().getString(R.string.outras));
                }
                else {
                    categoria.setText(despesa.getCategoria().getNome());
                }
                corView.setBackgroundColor(Color.parseColor(despesa.getCategoria().getCor()));
                DecimalFormat df = new DecimalFormat("0.00##");
                String result = "R$ " + df.format(despesa.getDespesa());
                valor.setText(result);
                data.setText(new SimpleDateFormat("dd/MM/yyyy").format(despesa.getData()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(despesa);
                        MyAdapterDespesas.ViewHolder.this.onClick();
                    }
                });
            } catch (NullPointerException ex){

            }
        }

        public void onClick() {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selectedPos);

            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }
}
