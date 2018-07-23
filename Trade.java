package com.jpm.Stock.Model;

import java.util.Calendar;
import java.util.Date;

public class Trade {

    private String entity;
    private char buySell;
    private double agreedFx;
    private String currency;
    private Date instructionDate;
    private Date settlementDate;
    private int units;
    private double pricePerUnit;
    private int day;
    private double usdAmount;

    public Trade(String entity, char buySell, double agreedFx, String currency, Date instructionDate,
                 Date settlementDate, int units, double pricePerUnit) {
        this.entity = entity;
        this.buySell = buySell;
        this.agreedFx = agreedFx;
        this.currency = currency;
        this.instructionDate = instructionDate;
        // this.settlementDate =settlementDate;
        this.units = units;
        this.pricePerUnit = pricePerUnit;
        this.settlementDate = settleTradeDate(settlementDate);
        this.usdAmount = calculateUSDAmount(this.pricePerUnit, this.units, this.agreedFx);
    }

    public double getAgreedFx() {
        return agreedFx;
    }

    public void setAgreedFx(double agreedFx) {
        this.agreedFx = agreedFx;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(double usdAmount) {
        this.usdAmount = usdAmount;
    }

    public String getEntity() {
        return entity;
    }

    public char getBuySell() {
        return buySell;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getInstructionDate() {
        return instructionDate;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public int getUnits() {
        return units;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setBuySell(char buySell) {
        this.buySell = buySell;
    }

    public void setAgreedFx(float agreedFx) {
        this.agreedFx = agreedFx;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setInstructionDate(Date instructionDate) {
        this.instructionDate = instructionDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Date settleTradeDate(Date settlementDate) {
        day = dayOfWeek(settlementDate);
        if (currency == "AED" || currency == "SAR") {
            if (day == 6 || day == 7) {
                int addDays = 8 - day;
                settlementDate = addDate(settlementDate, addDays);
            }
        } else {
            if (day == 1 || day == 7) {
                if (day == 7)
                    settlementDate = addDate(settlementDate, day - 5);
                else
                    settlementDate = addDate(settlementDate, day + 1);
            }
        }
        return settlementDate;
    }

    public Date addDate(Date date, int addDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addDays);
        return c.getTime();
    }

    public int dayOfWeek(Date date) {
        int dayOfWeek;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public double calculateUSDAmount(double pricePerUnit, int units, double agreedFx) {
        usdAmount = pricePerUnit * units * agreedFx;
        return usdAmount;
    }
}
