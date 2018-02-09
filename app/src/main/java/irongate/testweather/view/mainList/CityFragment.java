package irongate.testweather.view.mainList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import irongate.testweather.R;

/**
 * Created by Iron on 28.01.2018.
 */

public class CityFragment extends Fragment {
    private int id;
    private String name;
    private Integer temp;
    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (name != null)
            update();

        if (onClickListener != null)
            updateListener();
    }

    public void setFields(int id, String name, Integer temp) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        update();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        updateListener();
    }

    private void update() {
        View view = getView();
        if (view == null)
            return;

        view.setTag(id);

        TextView textName = view.findViewById(R.id.textListName);
        textName.setText(name);

        TextView textTemp = view.findViewById(R.id.textTemp);
        String tempStr = "...";
        if (temp != null) {
            tempStr = temp + " " + getResources().getString(R.string.degrees_celsius);

            if (temp > 0)
                tempStr = "+" + tempStr;
        }
        textTemp.setText(tempStr);
    }

    private void updateListener() {
        View view = getView();
        if (view == null)
            return;

        view.setOnClickListener(onClickListener);
    }
}
