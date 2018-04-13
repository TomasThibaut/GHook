package cn.gc.rangeseekbarexample.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cn.gc.rangeseekbarexample.BR;

/**
 * Created by 宫成 on 2018/4/13 下午5:48.
 *
 * This is done by assigning a @Bindable annotation to the getter and notifying in the setter.
 */

public class TemperatureData extends BaseObservable {
    private String location;
    private String celsius;

    public TemperatureData(String location, String celsius) {
        this.location = location;
        this.celsius = celsius;
    }

    @Bindable
    public String getCelsius() {
        return celsius;
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public  void setLocation(String location){
        this.location = location;
        //BR = binding Resource ? ?
        notifyPropertyChanged(BR.location);
    }

    public void setCelsius(String celsius) {
        this.celsius = celsius;
        notifyPropertyChanged(BR.celsius);
    }
}
