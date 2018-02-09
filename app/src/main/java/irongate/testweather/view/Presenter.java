package irongate.testweather.view;

import irongate.testweather.model.Main;

/**
 * Created by Iron on 28.01.2018.
 */

public class Presenter implements Main.Listener {
    private final IMainView view;

    Presenter(IMainView view) {
        this.view = view;
        Main.getInstance().setListener(this);
    }

    @Override
    public void onErrorCityAlreadyAdded() {
        view.showErrorCityAlreadyAdded();
    }

    @Override
    public void onErrorCityNotFound() {
        view.showErrorCityNotFound();
    }

    @Override
    public void onErrorCityData() {
        view.showErrorCityData();
    }
}
