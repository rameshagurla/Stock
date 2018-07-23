package com.jpm.Stock.Service;
import com.jpm.Stock.Model.Trade;
import java.util.*;

public class TradeServiceImpl {

    public List<Trade> process(List<Trade> trades) {
        for (int i = 0; i < trades.size(); i++) {
            for (int j = i + 1; j < trades.size(); j++) {
                if (trades.get(i).getEntity() == trades.get(j).getEntity() && j != i && trades.get(i).getBuySell() == trades.get(j).getBuySell()
                        && trades.get(i).getCurrency() == trades.get(j).getCurrency() && trades.get(i).getSettlementDate().equals(trades.get(j).getSettlementDate())) {
                    trades.get(i).setUsdAmount(trades.get(i).getUsdAmount() + trades.get(j).getUsdAmount());
                    trades.remove(j);
                }
            }
        }
        return trades;
    }

    public void rank(List<Trade> trades) {
        List<Trade> rankTrades = process(trades);
        Map<String, Double> buyRank = new HashMap<String, Double>();
        Map<String, Double> sellRank = new HashMap<String, Double>();
        for(int i = 0; i < rankTrades.size(); i++) {
            double buyAmt = 0, sellAmt = 0;
            if(rankTrades.get(i).getBuySell() == 'B') {
                if (buyRank.containsKey(rankTrades.get(i).getEntity())) {
                    buyAmt = buyRank.get(rankTrades.get(i).getEntity());
                    buyAmt += rankTrades.get(i).getUsdAmount();
                    buyRank.put(rankTrades.get(i).getEntity(), buyAmt);
                }
                else
                    buyRank.put(rankTrades.get(i).getEntity(), rankTrades.get(i).getUsdAmount());
            }
            else {
                if (sellRank.containsKey(rankTrades.get(i).getEntity())) {
                    sellAmt = buyRank.get(rankTrades.get(i).getEntity());
                    sellAmt += rankTrades.get(i).getUsdAmount();
                    sellRank.put(rankTrades.get(i).getEntity(), sellAmt);
                }
                else
                    sellRank.put(rankTrades.get(i).getEntity(), rankTrades.get(i).getUsdAmount());
            }
        }
        Set<Map.Entry<String, Double>> set  = buyRank.entrySet();
        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        System.out.println("\nRanking entity for buy instruction");
        System.out.println("===================================");
        int i = 1;
        for(Map.Entry<String, Double> entry:list){
            System.out.println("Ranking in outgoing entity " +entry.getKey()+" ==== "+entry.getValue() + " with rank " + i++);
        }
        Set<Map.Entry<String, Double>> setSell = sellRank.entrySet();
        List<Map.Entry<String, Double>> listSell = new ArrayList<Map.Entry<String, Double>>(setSell);
        Collections.sort( listSell, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        System.out.println("\nRanking entity for sell instruction");
        System.out.println("===================================");
        int j = 1;
        for(Map.Entry<String, Double> entry:listSell){
            System.out.println("Ranking in incoming entity " +entry.getKey()+" ==== "+entry.getValue() + " with rank " + j++);
        }
    }

    public Map<Date, Map<Character, Double>> settleAmt(List<Trade> trades) {
        Map<Date, Map<Character, Double>> map = new HashMap<Date, Map<Character, Double>>();
        for (int i = 0; i < trades.size(); i++) {
            double settledIncoming = 0, settledOutgoing = 0;
            if(trades.get(i).getBuySell() == 'B')
                settledOutgoing = trades.get(i).getUsdAmount();
            else
                settledIncoming = trades.get(i).getUsdAmount();
            for(int j = i + 1; j < trades.size(); j++) {
                if (trades.get(i).getSettlementDate().equals(trades.get(j).getSettlementDate()) && trades.get(i).getBuySell() == trades.get(j).getBuySell()) {
                    if (trades.get(i).getBuySell() == 'B') {
                        settledOutgoing += trades.get(j).getUsdAmount();
                        trades.remove(j);
                    }
                    else {
                        settledIncoming += trades.get(j).getUsdAmount();
                        trades.remove(j);
                    }
                }
            }
            Map<Character, Double> ch = new HashMap<Character, Double>();
            if (trades.get(i).getBuySell() == 'B') {
                ch.put('B',settledOutgoing);
                map.put(trades.get(i).getSettlementDate(), ch);
            }
            else {
                ch.put('S',settledIncoming);
                map.put(trades.get(i).getSettlementDate(), ch);
            }
        }
        return map;
    }
}
