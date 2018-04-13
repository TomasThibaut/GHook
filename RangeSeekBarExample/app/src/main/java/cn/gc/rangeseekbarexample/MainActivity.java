package cn.gc.rangeseekbarexample;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.gc.rangeseekbarexample.View.HorizonBarChart;
import cn.gc.rangeseekbarexample.View.Utils;
import cn.gc.rangeseekbarexample.View.VerticalBarChart;
import cn.gc.rangeseekbarexample.databinding.LayoutDatabingBinding;
import cn.gc.rangeseekbarexample.databinding.MainActivityContract;
import cn.gc.rangeseekbarexample.databinding.MainActivityPresenter;
import cn.gc.rangeseekbarexample.databinding.TemperatureData;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{
//public class MainActivity extends AppCompatActivity {
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chengjidan);
//        layout = findViewById(R.id.layout);
//        layout.setBackground(null);
//        layout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
//        ListView list = findViewById(R.id.list);
//        List<String> data = new ArrayList<>();
//        for (int i = 0; i < 200; i++) {
//            data.add(String.valueOf(i));
//        }
//        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
//        Button button = new Button(this);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
//        button.setLayoutParams(layoutParams);
//        button.setBackgroundResource(R.mipmap.slider);
//        View view = (View) list.getAdapter().getItem(0);
//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//        lp.topMargin = -300;
//        view.setLayoutParams(lp);
//        list.addHeaderView(button);
//        VerticalBarChart chart = (VerticalBarChart) findViewById(R.id.chart);
//        chart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"xxx",Toast.LENGTH_SHORT).show();
//                layout.setBackground(null);
//            }
//        });
//
//        List<SparseArray<Float>> list  = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            SparseArray<Float> s = new SparseArray<>();
//            s.append(0,2323f);
//            s.append(1,1399f);
//            list.add(s);
//        }
//        chart.setBarPersents(list);

//        HorizonBarChart chart = findViewById(R.id.hChart);
//        chart.setNumber(80, 100, 1230, Color.parseColor("#ffb02a"),Color.parseColor("#ffc738"), Color.YELLOW);


//        List<String> data = new ArrayList<>();

//        for (int i = 0; i < 100; i++) {
//            data.add("" + i);
//        }
//        ListView list = (ListView) findViewById(R.id.mainList);
//        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
//        View inflate = LayoutInflater.from(this).inflate(R.layout.layout, list, false);
//        VerticalBarChart verticalBarChart = inflate.findViewById(R.id.verticalbarchart);
//        List<float[]> alist  = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            SparseArray<Float> s = new SparseArray<>();
//            float[] arr = new float[2];
//            arr[0] = 1399f;
//            arr[1] = 3231f;
//            alist.add(arr);
//        }
//        chart.setBarPersents(alist);
//        list.addHeaderView(inflate);
//        HorizonBarChart horizonbarchart = inflate.findViewById(R.id.horizonbarchart);
//        horizonbarchart. setNumber(80,100,1299);

//        slideTab();
        databinding();
    }

    private void databinding() {
        MainActivityPresenter presenter = new MainActivityPresenter(this , this);
        LayoutDatabingBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_databing);
        binding.setTemp(new TemperatureData("Beijing", "18"));
        binding.setPresenter(presenter);
    }

    @Override
    public void showData(TemperatureData temperatureData) {
        Toast.makeText(this, temperatureData.getCelsius(), Toast.LENGTH_SHORT).show();
    }

    private void slideTab() {
        ViewPager viewPagerUp = findViewById(R.id.viewPagerUp);
        viewPagerUp.setPageMargin((int) Utils.dip2px(this,25));
        viewPagerUp.setOffscreenPageLimit(10);
        viewPagerUp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("G_C", "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPager viewPagerDown = findViewById(R.id.viewPagerDown);
        viewPagerUp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View inflateView = LayoutInflater.from(MainActivity.this).inflate(R.layout.up_page_item, container, false);
                container.addView(inflateView);
                return inflateView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }



}
