package irongate.testweather.view.mainList;

import java.util.List;

import irongate.testweather.model.ListCity;
import irongate.testweather.model.Main;

/**
 * Created by Iron on 05.02.2018.
 */

public class MainListPresenter implements Main.ListListener{
    private IMainListView view;

    MainListPresenter(IMainListView view) {
        this.view = view;
        Main.getInstance().setListListener(this);
    }

    List<ListCity> getCitiesList() {
        return Main.getInstance().getCitiesList();
    }

    void addNewCity(String name) {
        Main.getInstance().addNewCity(name);
    }

    @Override
    public void onListChanged() {
        view.updateCityList();
    }

    @Override
    public void onUpdateError() {
        view.onUpdateError();
    }
}
