package com.vanguard.backend.portfolio;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final YahooFinanceService yahooFinanceService;

    public PortfolioService(PortfolioRepository portfolioRepository, YahooFinanceService yahooFinanceService) {
        this.portfolioRepository = portfolioRepository;
        this.yahooFinanceService = yahooFinanceService;
    }

    public List<Asset> getPortfolioAssets() {
        List<Asset> assets = portfolioRepository.findAll();
        for (Asset asset : assets) {
            YahooFinanceService.YahooQuote quote = yahooFinanceService.getQuote(asset.getSymbol());
            if (quote != null) {
                asset.setCurrentPrice(quote.price());
                asset.setValue(asset.getQuantity() * quote.price());
            } else {
                asset.setCurrentPrice(0.0);
                asset.setValue(0.0);
            }
        }
        return assets;
    }

    public Asset addAsset(String symbol, double quantity) {
        YahooFinanceService.YahooQuote quote = yahooFinanceService.getQuote(symbol);
        if (quote == null) {
            throw new IllegalArgumentException("Invalid symbol or unable to fetch data for: " + symbol);
        }
        
        Optional<Asset> existing = portfolioRepository.findBySymbol(symbol.toUpperCase());
        Asset asset;
        if (existing.isPresent()) {
            asset = existing.get();
            asset.setQuantity(asset.getQuantity() + quantity);
        } else {
            asset = new Asset();
            asset.setSymbol(symbol.toUpperCase());
            asset.setName(quote.name());
            asset.setQuantity(quantity);
            String cat = "ETF".equalsIgnoreCase(quote.quoteType()) ? "ETF" : "Stock";
            asset.setCategory(cat);
        }
        return portfolioRepository.save(asset);
    }

    public List<YahooFinanceService.YahooSearchResult> searchAssets(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return yahooFinanceService.searchTickers(query);
    }
}
