package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.UserExpenseStatistic;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticActivity extends AppCompatActivity {
    private BarChart barChart;
    private TextView textViewMonthlyExpenseStat;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinner;
    private TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        barChart = findViewById(R.id.barChart);
        textViewMonthlyExpenseStat = findViewById(R.id.textViewMonthlyStatistic);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        spinner = findViewById(R.id.spinnerYear);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        // Gia su 2019 la nam he thong start up
        List<Integer> years = new ArrayList<>();
        for(int i=year;i>=2019;i--){
            years.add(i);
        }
        spinner.setAdapter(new ArrayAdapter(this,R.layout.item_text_spinner,years));
        tokenManager = new TokenManager(this);

        // Luc moi vao man hinh goi api hien ra thong ke
        callAPIGetMonthlyStats(year);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinner.getSelectedItem().toString();
                int selectedYear = Integer.parseInt(selectedItem);
                callAPIGetMonthlyStats(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPIGetMonthlyStats(year);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void callAPIGetMonthlyStats(int year){
        APIUtils.getApiServiceInterface().getMonthlyExpenseStatisticsOfUser("Bearer "+tokenManager.getAccessToken(),
                year).enqueue(new Callback<List<UserExpenseStatistic>>() {
            @Override
            public void onResponse(Call<List<UserExpenseStatistic>> call, Response<List<UserExpenseStatistic>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<UserExpenseStatistic> statisticList = response.body();

                    updateBarChartUI(statisticList, year);
                }
            }

            @Override
            public void onFailure(Call<List<UserExpenseStatistic>> call, Throwable t) {

            }
        });
    }

    private void updateBarChartUI(List<UserExpenseStatistic> expenseStatisticList, int year) {
        textViewMonthlyExpenseStat.setText("User's Monthly Expense "+year);
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            UserExpenseStatistic userExpenseStatistic = expenseStatisticList.get(i);
            BarEntry barEntry = new BarEntry(i, userExpenseStatistic.getExpenseSumOfMonth());
            barEntries.add(barEntry);
        }


        BarDataSet barDataSet = new BarDataSet(barEntries, "User Expenses (Unit $)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(1000);
        barChart.getDescription().setText("User Expenses Of " + year);
        barChart.getDescription().setTextColor(R.color.white);
        barChart.getDescription().setTextSize(16);
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("April");
        xAxisLabel.add("May");
        xAxisLabel.add("June");
        xAxisLabel.add("July");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sep");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        barChart.getXAxis().setLabelCount(xAxisLabel.size());

        YAxis yAxisleft = barChart.getAxisLeft();
        yAxisleft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return value+" $";
            }
        });

        YAxis yAxisright = barChart.getAxisRight();
        yAxisright.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return "";
            }
        });
    }
}