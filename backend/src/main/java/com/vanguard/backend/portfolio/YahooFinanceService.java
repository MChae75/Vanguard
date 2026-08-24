package com.vanguard.backend.portfolio;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.List;

@Service
public class YahooFinanceService {
    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<String> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        return new HttpEntity<>(headers);
    }

    public YahooQuote getQuote(String symbol) {
        try {
            yahoofinance.Stock stock = yahoofinance.YahooFinance.get(symbol);
            if (stock != null && stock.getQuote() != null && stock.getQuote().getPrice() != null) {
                return new YahooQuote(
                        symbol,
                        stock.getName() != null ? stock.getName() : symbol,
                        stock.getQuote().getPrice().doubleValue(),
                        "EQUITY"
                );
            }
        } catch (Exception e) {
            System.err.println("Yahoo API Library failed for: " + symbol + " - " + e.getMessage());
        }
        // Fallback mock data if Yahoo Finance blocks the request
        return new YahooQuote(symbol, symbol + " (Mock)", 100.0 + (Math.random() * 50), "EQUITY");
    }

    public List<YahooSearchResult> searchTickers(String query) {
        try {
            String url = "https://query2.finance.yahoo.com/v1/finance/search?q=" + query + "&quotesCount=5";
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, getEntity(), Map.class);
            Map response = resp.getBody();
            if (response != null && response.containsKey("quotes")) {
                List<Map<String, Object>> quotes = (List<Map<String, Object>>) response.get("quotes");
                return quotes.stream().map(q -> new YahooSearchResult(
                        (String) q.get("symbol"),
                        (String) q.getOrDefault("shortname", q.get("longname")),
                        (String) q.getOrDefault("quoteType", "EQUITY"),
                        (String) q.getOrDefault("exchDisp", "")
                )).toList();
            }
        } catch (Exception e) {
            System.err.println("Search API failed (Anti-Bot block). Using mock data for: " + query);
        }
        // Fallback mock search result
        return List.of(new YahooSearchResult(query.toUpperCase(), query.toUpperCase() + " Inc. (Mock)", "EQUITY", "MOCK"));
    }

    public record YahooQuote(String symbol, String name, double price, String quoteType) {}
    public record YahooSearchResult(String symbol, String name, String type, String exchange) {}
}
