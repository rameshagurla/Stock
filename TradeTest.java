package com.jpm.Stock;
import com.jpm.Stock.Main.DateFormat;
import com.jpm.Stock.Model.Trade;
import com.jpm.Stock.Service.TradeServiceImpl;
import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import static junit.framework.TestCase.assertTrue;

public class TradeTest {

    DateFormat dateFormat = new DateFormat();
    Trade trade = new Trade("foo", 'B', 0.5, "USD", new java.util.Date(System.currentTimeMillis()),
            new java.util.Date(System.currentTimeMillis()), 1, 2);
    Trade trade1 = new Trade("foo", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 200, 100.25);
    Trade trade2 = new Trade("bar", 'S', 0.22, "AED", dateFormat.parse("05-Jan-2016"), dateFormat.parse("09-Jan-2016"), 450, 150.50);
    Trade trade3 = new Trade("foo", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 200, 120.25);
    Trade trade4 = new Trade("foo", 'S', 0.50, "SGP", dateFormat.parse("10-Jan-2017"), dateFormat.parse("10-Jan-2017"), 200, 110.25);
    List<Trade> list = Arrays.asList(trade1,trade2,trade3,trade4);

    //This test is used to get the amount USD settled in both buy and sell instructions
    @Test
    public void settleAmtTest() {
        TradeServiceImpl ts = new TradeServiceImpl();
        double x = 0, y = 0, result = 0;
        Map<Date, Map<Character, Double>> settleIncoming = ts.settleAmt(list);
        for (Map.Entry<Date, Map<Character, Double>> entry : settleIncoming.entrySet())
        {
            Date date = entry.getKey();
            Map<Character, Double> map1 = entry.getValue();
            for(Map.Entry<Character, Double> map : map1.entrySet()) {
                if(map.getKey() == 'S')
                    x = map.getValue();
                else
                    y = map.getValue();
            }
        }
        result = x + y;
        assertTrue(result == 47974.50);
    }

    //This test is used to sum up the trades which are equal in their entity name, buysell, currency and trade on same date(trade 1 and trade 3 will be treated as one trade)
    @Test
    public void processTest() {
        TradeServiceImpl ts = new TradeServiceImpl();
        int size = ts.process(list).size();
        assertTrue(size == 3);
    }

    //This test is used to calculate the USD amount which is unit price * units * agreedFx
    @Test
    public void calculateUSDAmountTest() {
        double result = trade.calculateUSDAmount(1.0, 2, 0.5);
        assertTrue(result == 1);
    }

    //This test is used for the settlement date
    @Test
    public void settleTradeDateTest() {
        Date result = trade.settleTradeDate(new java.util.Date(System.currentTimeMillis()));
        int year = result.getYear();
        assertTrue(result.getYear() == 118);
    }

    //This test is used to return the day of week for a particular given trade
    @Test
    public void dayOfWeekTest() {
        int result = trade.dayOfWeek(new java.util.Date(System.currentTimeMillis()));
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        assertTrue(result == dayOfWeek);
    }

}
