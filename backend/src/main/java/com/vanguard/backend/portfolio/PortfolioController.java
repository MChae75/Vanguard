package com.vanguard.backend.portfolio;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return portfolioService.getPortfolioAssets();
    }

    @PostMapping("/add")
    public Asset addAsset(@RequestBody AddAssetRequest request) {
        return portfolioService.addAsset(request.getSymbol(), request.getQuantity());
    }

    @GetMapping("/search")
    public List<YahooFinanceService.YahooSearchResult> searchAssets(@RequestParam String q) {
        return portfolioService.searchAssets(q);
    }
}

class AddAssetRequest {
    private String symbol;
    private double quantity;
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
