package irongate.testweather.view.mainList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import irongate.testweather.R;
import irongate.testweather.model.ListCity;
import irongate.testweather.view.details.DetailsView;

/**
 * Created by Iron on 05.02.2018.
 */

public class MainListView implements IMainListView, View.OnClickListener {
    private final DetailsView detailsView;
    private Activity activity;
    private MainListPresenter presenter;

    private ArrayList<CityFragment> cityFragments = new ArrayList<>();

    public MainListView(Activity activity) {
        this.activity = activity;
        presenter = new MainListPresenter(this);

        detailsView = new DetailsView(activity);
    }

    @Override
    public void updateCityList() {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        for (CityFragment fragment:cityFragments) {
            ft.remove(fragment);
        }
        ft.commit();
        cityFragments = new ArrayList<>();

        List<ListCity> list = presenter.getCitiesList();
        if (list == null)
            return;

        ArrayList<ListCity> sorted = new ArrayList<>(list);
        Collections.sort(sorted, new Comparator<ListCity>() {
            @Override
            public int compare(ListCity o2, ListCity o1) {
                return o2.name.compareTo(o1.name);
            }
        });
        for (ListCity listCity:sorted) {
            CityFragment cityFragment = new CityFragment();
            cityFragment.setFields(listCity.id, listCity.name, listCity.temp);
            cityFragment.setOnClickListener(this);
            activity.getFragmentManager().beginTransaction().add(R.id.mainLinearLayout, cityFragment).commit();
            cityFragments.add(cityFragment);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateButtonAdd();
            }
        });
    }

    @Override
    public void onUpdateError() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Error info update!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateButtonAdd() {
        LinearLayout lr = activity.findViewById(R.id.mainLinearLayout);
        for (int i = 0; i < lr.getChildCount(); i++) {
            View child = lr.getChildAt(i);
            if (child instanceof Button) {
                lr.removeView(child);
                break;
            }
        }
        Button button = new Button(activity);
        button.setText(R.string.btn_add_city);
        lr.addView(button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // У нас только одна кнопка (добавить город)
        if (v instanceof Button) {
            onBtnAddCity();
            return;
        }

        // А тут только города из списка
        if (v instanceof RelativeLayout) {
            int id = (int) v.getTag();
            detailsView.showDetails(id);
        }
    }

    private void onBtnAddCity() {
        if (presenter.getCitiesList().size() >= 20){
            Toast.makeText(activity, "Max number of cities 20!", Toast.LENGTH_LONG).show();
            return;
        }
        @SuppressLint("InflateParams")
        final View alertView = LayoutInflater.from(activity).inflate(R.layout.alert_new_city, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(alertView);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        String name = ((EditText) alertView.findViewById(R.id.editText)).getText().toString();
                        presenter.addNewCity(name);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}
