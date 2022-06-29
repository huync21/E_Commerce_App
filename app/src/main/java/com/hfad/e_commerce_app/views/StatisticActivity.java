package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.OrdersAdapter;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.models.UserExpenseByCategoryStats;
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
    private PieChart pieChart;
    private TextView textViewMonthlyExpenseStat;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerYear, spinnerYear2, spinnerMonth;
    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    private TokenManager tokenManager;

    private List<Order> mListOrder = new ArrayList<>();
    // bien year la cho chuc nang thong ke thu nhat, bien year1 va month la cho chuc nang thong ke thu 2
    private int year, year1, month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        textViewMonthlyExpenseStat = findViewById(R.id.textViewMonthlyStatistic);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerYear2 = findViewById(R.id.spinnerYearCat);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        recyclerViewOrders = findViewById(R.id.recycler_view_orders);

        ordersAdapter = new OrdersAdapter(mListOrder);
        recyclerViewOrders.setAdapter(ordersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewOrders.setLayoutManager(linearLayoutManager);

        year = Calendar.getInstance().get(Calendar.YEAR);
        year1 = year;
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        // Gia su 2019 la nam he thong start up
        List<Integer> years = new ArrayList<>();
        for (int i = year; i >= 2019; i--) {
            years.add(i);
        }
        spinnerYear.setAdapter(new ArrayAdapter(this, R.layout.item_text_spinner, years));
        spinnerYear2.setAdapter(new ArrayAdapter(this, R.layout.item_text_spinner, years));
        spinnerMonth.setAdapter(new ArrayAdapter(this,R.layout.item_text_spinner,getResources().getStringArray(R.array.months)));
        spinnerMonth.setSelection(month-1);
        tokenManager = new TokenManager(this);

        // Luc moi vao man hinh goi api hien ra thong ke
        callAPIGetMonthlyStats(year);
        callAPIGetStatisticByCategory(year1,month);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerYear.getSelectedItem().toString();
                int selectedYear = Integer.parseInt(selectedItem);
                year = selectedYear;
                callAPIGetMonthlyStats(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerYear2.getSelectedItem().toString();
                int selectedYear = Integer.parseInt(selectedItem);
                year1 = selectedYear;
                callAPIGetStatisticByCategory(year1,month);
                callAPIGetAllOrdersByYearAndMonth(year1, month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = spinnerMonth.getSelectedItemPosition()+1;
                callAPIGetStatisticByCategory(year1,month);
                callAPIGetAllOrdersByYearAndMonth(year1, month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                year = Calendar.getInstance().get(Calendar.YEAR);
                year1 = year;
                month = Calendar.getInstance().get(Calendar.MONTH)+1;
                callAPIGetMonthlyStats(year);
                callAPIGetStatisticByCategory(year1,month);
                callAPIGetAllOrdersByYearAndMonth(year1, month);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ordersAdapter.setItemClickedListener(new OrdersAdapter.ItemClickedListener() {
            @Override
            public void onItemClickedListener(Order order) {
                Intent orderDetailIntent = new Intent(StatisticActivity.this, OrderDetailActivity.class);
                orderDetailIntent.putExtra("order", order);
                startActivity(orderDetailIntent);
            }
        });
    }


    private void callAPIGetMonthlyStats(int year) {
        APIUtils.getApiServiceInterface().getMonthlyExpenseStatisticsOfUser("Bearer " + tokenManager.getAccessToken(),
                year).enqueue(new Callback<List<UserExpenseStatistic>>() {
            @Override
            public void onResponse(Call<List<UserExpenseStatistic>> call, Response<List<UserExpenseStatistic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserExpenseStatistic> statisticList = response.body();

                    updateBarChartUI(statisticList, year);
                }
            }

            @Override
            public void onFailure(Call<List<UserExpenseStatistic>> call, Throwable t) {

            }
        });
    }

    private void callAPIGetStatisticByCategory(int year, int month){
        APIUtils.getApiServiceInterface().getExpenseStatisticsByCategoryOfUser("Bearer "+tokenManager.getAccessToken(),
                year,month).enqueue(new Callback<List<UserExpenseByCategoryStats>>() {
            @Override
            public void onResponse(Call<List<UserExpenseByCategoryStats>> call, Response<List<UserExpenseByCategoryStats>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<UserExpenseByCategoryStats> list = response.body();

                    updatePieChartUI(list,year);
                }
            }

            @Override
            public void onFailure(Call<List<UserExpenseByCategoryStats>> call, Throwable t) {

            }
        });
    }

    private void callAPIGetAllOrdersByYearAndMonth(int year, int month){
        APIUtils.getApiServiceInterface().getAllOrdersByMonthAndYear("Bearer "+tokenManager.getAccessToken(),year, month)
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            mListOrder = response.body();
                            ordersAdapter.setmListOrders(mListOrder);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {

                    }
                });
    }

    private void updateBarChartUI(List<UserExpenseStatistic> expenseStatisticList, int year) {
        textViewMonthlyExpenseStat.setText("User's Monthly Expense " + year);
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            UserExpenseStatistic userExpenseStatistic = expenseStatisticList.get(i);
            BarEntry barEntry = new BarEntry(i, userExpenseStatistic.getExpenseSumOfMonth());
            barEntries.add(barEntry);
        }


        BarDataSet barDataSet = new BarDataSet(barEntries, "User Expenses (Unit $)");
        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(15f);
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

                return value + " $";
            }
        });

        YAxis yAxisright = barChart.getAxisRight();
        yAxisright.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return "";
            }
        });
        barChart.invalidate();
    }

    private void updatePieChartUI(List<UserExpenseByCategoryStats> expenseByCategoryStatsList,int year) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        int i=0;
        int total = expenseByCategoryStatsList.get(0).getTotalOfUser();
        for (UserExpenseByCategoryStats userExpenseByCategoryStats : expenseByCategoryStatsList) {
            PieEntry pieEntry = new PieEntry(userExpenseByCategoryStats.getTotal(), userExpenseByCategoryStats.getCategory());
            pieEntries.add(pieEntry);
            i++;
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "     (Unit: $)");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(20f);
        pieChart.getDescription().setText("By Category Year "+year);
        pieChart.getDescription().setTextColor(R.color.white);
        pieChart.getDescription().setTextSize(16);
        pieChart.setData(new PieData(pieDataSet));


        pieChart.setCenterText("Total: "+total+"$");
        pieChart.setCenterTextSize(20f);
        pieChart.invalidate();
    }
}