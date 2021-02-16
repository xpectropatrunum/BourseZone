package ir.sourcearena.boursezone.filteryab.simple.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.R;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<AddSegments> utils;
    private List<AddSegments> data;
    Settings setting;
    RecyclerAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AddSegments item);
    }

    public RecyclerAdapter(Context context, List utils, RecyclerAdapter.OnItemClickListener listener) {
        this.context = context;
        this.utils = utils;
        this.data = utils;

        setFirst_spinner();
        setSecond_spinner();
        this.listener = listener;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addsimplefilterlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    AddSegments pu;


    List<String> first_spinner, seconds_spinner;

    public void setFirst_spinner() {
        first_spinner = new ArrayList<>();
        first_spinner.add("تعداد معاملات");
        first_spinner.add("حجم معاملات");
        first_spinner.add("ارزش معاملات");
        first_spinner.add("قیمت دیروز");
        first_spinner.add("اولین قیمت");
        first_spinner.add("کمترین قیمت");
        first_spinner.add("بیشترین قیمت");
        first_spinner.add("آخرین قیمت");
        first_spinner.add("تغییر آخرین قیمت");
        first_spinner.add("درصد تغییر آخرین قیمت");
        first_spinner.add("قیمت پایانی");
        first_spinner.add("تغییر قیمت پایانی");
        first_spinner.add("درصد تغییر قیمت پایانی");
        first_spinner.add("eps");
        first_spinner.add("p/e");
        first_spinner.add("آستانه مجاز پایین");
        first_spinner.add("آستانه مجاز بالا");
        first_spinner.add("تعداد سهام");
        first_spinner.add("ارزش بازار");
        first_spinner.add("قیمت خرید - سطر اول");
        first_spinner.add("تعداد خریدار - سطر اول");
        first_spinner.add("حجم خرید- سطر اول");
        first_spinner.add("قیمت فروش - سطر اول");
        first_spinner.add("تعداد فروشنده - سطر اول");
        first_spinner.add("حجم فروش- سطر اول");
        first_spinner.add("قیمت خرید - سطر دوم");
        first_spinner.add("تعداد خریدار - سطر دوم");
        first_spinner.add("حجم خرید- سطر دوم");
        first_spinner.add("قیمت فروش - سطر دوم");
        first_spinner.add("تعداد فروشنده - سطر دوم");
        first_spinner.add("حجم فروش- سطر دوم");
        first_spinner.add("قیمت خرید - سطر سوم");
        first_spinner.add("تعداد خریدار - سطر سوم");
        first_spinner.add("حجم خرید- سطر سوم");
        first_spinner.add("قیمت فروش - سطر سوم");
        first_spinner.add("تعداد فروشنده - سطر سوم");
        first_spinner.add("حجم فروش- سطر سوم");
        first_spinner.add("حجم مبنا");
        first_spinner.add("تعداد خریدار حقیقی");
        first_spinner.add("تعداد خریدار حقوقی");
        first_spinner.add("حجم خرید حقیقی");
        first_spinner.add("حجم خرید حقوقی");
        first_spinner.add("تعداد فروشنده حقیقی");
        first_spinner.add("تعداد فروشنده حقوقی");
        first_spinner.add("حجم فروش حقیقی");
        first_spinner.add("حجم فروش حقوقی");

    }

    public void setSecond_spinner() {
        seconds_spinner = new ArrayList<>();
        seconds_spinner.add("برابر");
        seconds_spinner.add("بزرگتر مساوی");
        seconds_spinner.add("کوچکتر مساوی");
        seconds_spinner.add("بزرگتر");
        seconds_spinner.add("کوچکتر");
    }

    int last_pos = 0;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setTag(utils.get(position));


        final Typeface tf = ResourcesCompat.getFont(context,R.font.iransansmedium);
        pu = utils.get(position);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, first_spinner){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.text1,null));
                text.setTypeface(tf);
                text.setTextSize(14);
                return view;
            }
        };
        holder.condition.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, seconds_spinner){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.text1,null));
                text.setTypeface(tf);
                text.setTextSize(14);
                return view;
            }
        };
        holder.function.setAdapter(adapter2);

        holder.value.setTypeface(tf);
        holder.value.setTextSize(14);
        holder.value.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.text1,null));
        holder.condition.setSelection(pu.getSp1());
        holder.function.setSelection(pu.getSp2());
        holder.value.setText(pu.getVal() + "");
        holder.row.setText((position + 1) + "");
        last_pos = position;
        holder.remove.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);


        holder.condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                last_pos = position;
                if(i != utils.get(last_pos).getSp1()) {
                    utils.get(last_pos).setSp1(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                last_pos = position;
            }
        });
        holder.function.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                last_pos = position;
                if(i != utils.get(last_pos).getSp2()) {
                    utils.get(last_pos).setSp2(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                last_pos = position;
            }
        });
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                last_pos = position;

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = holder.value.getText().toString();

                        utils.get(last_pos).setVal(text);


            }
        });

        holder.bind(pu, listener);

    }

    @Override
    public int getItemCount() {
        return utils.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public Spinner condition;
        public TextView row;
        public Spinner function;
        public EditText value;
        public ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);

            condition = itemView.findViewById(R.id.sp1);
            function = itemView.findViewById(R.id.sp2);
            value = itemView.findViewById(R.id.ed1);
            row = itemView.findViewById(R.id.row_number);
            remove = itemView.findViewById(R.id.btn_remove_filter);


        }

        public void bind(final AddSegments item, final OnItemClickListener listener) {

            final int pos = utils.indexOf(item);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btn_remove_filter && utils.indexOf(item) !=0) {
                        listener.onItemClick(item);
                    }





                }
            });
            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    last_pos = pos;




                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(v.getId() != R.id.btn_remove_filter){
                        last_pos = pos;

                    }






                }
            });
        }
    }


}