package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserExpenseStatistic implements Serializable {
    @SerializedName("month")
    @Expose
    private int month;
    @SerializedName("expense_sum_of_month")
    @Expose
    private int expenseSumOfMonth;
    @SerializedName("percent_expense")
    @Expose
    private float percentExpense;

    public UserExpenseStatistic() {
    }

    public UserExpenseStatistic(int month, int expenseSumOfMonth, float percentExpense) {
        this.month = month;
        this.expenseSumOfMonth = expenseSumOfMonth;
        this.percentExpense = percentExpense;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getExpenseSumOfMonth() {
        return expenseSumOfMonth;
    }

    public void setExpenseSumOfMonth(int expenseSumOfMonth) {
        this.expenseSumOfMonth = expenseSumOfMonth;
    }

    public float getPercentExpense() {
        return percentExpense;
    }

    public void setPercentExpense(float percentExpense) {
        this.percentExpense = percentExpense;
    }
}
