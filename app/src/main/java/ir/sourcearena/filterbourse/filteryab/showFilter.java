package ir.sourcearena.filterbourse.filteryab;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.baoyz.widget.PullRefreshLayout;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.voela.actrans.AcTrans;
import ir.sourcearena.filterbourse.FieldsUtil;
import ir.sourcearena.filterbourse.FilterShowActivity;
import ir.sourcearena.filterbourse.FilterUtils;
import ir.sourcearena.filterbourse.NamadRouter;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.ui.watcher.RecyclerAdapter;
import ir.sourcearena.filterbourse.Settings;
import ir.sourcearena.filterbourse.ui.watcher.Utils;
import mehdi.sakout.fancybuttons.FancyButton;

public class showFilter extends Fragment {
    RecyclerView rv ;
    View root;
    JSONArray ja;
    RecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    FieldsUtil fu;
    String name;

    List<Utils> utils;

    public showFilter(JSONArray ja, String name)  {
        this.ja = ja;
        this.name = name;


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.user_filtershow, container, false);
        try {
            setUtils();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        defRecycle();
        FancyButton btn = root.findViewById(R.id.btn_back);
        TextView tv =  root.findViewById(R.id.filter_name);
        tv.setText(name);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Fragment fragment = new AddFilter();



               FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.filter_show, fragment);


                ft.commit();



            }
        });
        return root;


    }
    public void defRecycle(){

        rv = root.findViewById(R.id.recycleViewContainer);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(getContext(), null,utils, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Utils item) {
                String dl = " \\(";
                String name = item.getName().split(dl)[0];
                name = name.replace(".", " ");
                name = name.split("  ")[1];

                Intent in = new Intent(getActivity(), NamadRouter.class);
                in.putExtra(Settings.TITLE_EXTRA, name);
                in.putExtra(Settings.APPEND_URL, "&name=" + name);
                startActivity(in);
                new AcTrans.Builder(getActivity()).performFade();
            }


        },null);
        rv.setAdapter(mAdapter);
        rv.setFocusable(true);
        rv.requestFocus();

    }
    public void setUtils() throws JSONException {
        utils = new ArrayList<>();
        for(int i= 0; i< ja.length(); i++){
            JSONObject obj = ja.getJSONObject(i);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString = formatter.format(Float.parseFloat(obj.getString("close_price")));
            utils.add(new Utils((i + 1) + ". " + obj.getString("name"), obj.getString("full_name"),
                    obj.getString("market"), yourFormattedString , obj.getString("close_price_change"), obj.getString("close_price_change_percent")+"%"));

        }

    }
}
