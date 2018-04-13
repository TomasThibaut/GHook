package cn.gc.rangeseekbarexample.databinding;

/**
 * Created by 宫成 on 2018/4/13 下午5:52.
 */

public interface MainActivityContract {
    interface Presenter {
        void onShowData(TemperatureData temperatureData);
    }

    interface View {
        void showData(TemperatureData temperatureData);
    }

}
