package cn.gc.rangeseekbarexample.databinding;

import android.content.Context;

/**
 * Created by 宫成 on 2018/4/13 下午5:53.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private MainActivityContract.View view;
    private Context ctx;
    public MainActivityPresenter(MainActivityContract.View view, Context context) {
        this.view  = view;
        this.ctx = context;
    }

    @Override
    public void onShowData(TemperatureData temperatureData) {
        if (view != null) {
            view.showData(temperatureData);
        }
    }
}
