package com.jpm.Stock.Main;
import com.jpm.Stock.Model.Trade;
import com.jpm.Stock.Service.TradeServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TradeMain {

    public static void main(String args[]) {
        DateFormat dateFormat = new DateFormat();
        //Sample data created
        List<Trade> trade = new ArrayList<Trade>();
        trade.add(new Trade("foo", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 200, 100.25));
        trade.add(new Trade("bar", 'S', 0.22, "AED", dateFormat.parse("05-Jan-2016"), dateFormat.parse("09-Jan-2016"), 450, 150.50));
        trade.add(new Trade("foo", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 200, 100.25));
        trade.add(new Trade("foo", 'S', 0.50, "SGP", dateFormat.parse("10-Jan-2017"), dateFormat.parse("10-Jan-2017"), 200, 100.25));
        trade.add(new Trade("abc", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 200, 100.25));
        trade.add(new Trade("cde", 'S', 0.22, "AED", dateFormat.parse("05-Jan-2016"), dateFormat.parse("09-Jan-2016"), 550, 150.50));
        trade.add(new Trade("edg", 'B', 0.50, "SGP", dateFormat.parse("01-Jan-2016"), dateFormat.parse("02-Jan-2016"), 300, 100.25));
        trade.add(new Trade("skf", 'S', 0.50, "SGP", dateFormat.parse("10-Jan-2017"), dateFormat.parse("10-Jan-2017"), 400, 100.25));

        //Given trades
        System.out.println("\nTrades given as input");
        System.out.println("==========================");
        trade.forEach(t->System.out.println(t.getEntity() + " \t " + t.getBuySell() + " \t " + t.getCurrency() + " \t " + t.getUsdAmount() ));

        //Settlement dates for these trades based on their Currency
        System.out.println("\nSettlement dates for given trades");
        System.out.println("==================================");
        trade.forEach(t->System.out.println(t.getSettlementDate()));

        TradeServiceImpl tsImpl = new TradeServiceImpl();
        List<Trade> trading = tsImpl.process(trade);

        //Adding amount if settlementDate is same for a particular entity and Buy/Sell
        System.out.println("\nEntity \t BuySell \tUsdAmount \t SettlementDate");
        System.out.println("=======  ========   =========   ================");
        trading.forEach(t->System.out.println(t.getEntity() + " \t\t " + t.getBuySell() + " \t \t" + t.getUsdAmount() + " \t " + t.getSettlementDate()));

        //Rankings of entities
        tsImpl.rank(trade);

        //Amount Settled incoming and outgoing everyday
        System.out.println("\nAmount in USD settled everyday");
        System.out.println("================================");
        Map<Date, Map<Character, Double>> settleIncoming = tsImpl.settleAmt(trade);
        for (Map.Entry<Date, Map<Character, Double>> entry : settleIncoming.entrySet())
        {
            Date date = entry.getKey();
            Map<Character, Double> map1 = entry.getValue();
            for(Map.Entry<Character, Double> map : map1.entrySet()) {
                if(map.getKey() == 'S')
                    System.out.println("Amount in USD settled incoming on date " + entry.getKey() + " and amount " + map.getValue());
                else
                    System.out.println("\nAmount in USD settled outgoing on date " + entry.getKey() + " and amount " + map.getValue());
            }
        }
    }
}