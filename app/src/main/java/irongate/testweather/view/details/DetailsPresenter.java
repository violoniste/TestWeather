package irongate.testweather.view.details;

import irongate.testweather.model.Main;

/**
 * Created by Iron on 05.02.2018.
 */

class DetailsPresenter implements Main.DetailsListener {
    private IDetailsView view;

    DetailsPresenter(IDetailsView detailsView) {
        this.view = detailsView;
        Main.getInstance().setDetailListener(this);
    }

    String getNameById(int id) {
        return Main.getInstance().getNameById(id);
    }

    void getCurrent(int id) {
        Main.getInstance().getCurrent(id);
    }

    @Override
    public void onCurrent(String current) {
        view.onCurrent(current);
    }

    void removeCity(int id) {
        Main.getInstance().removeCity(id);
    }
}
