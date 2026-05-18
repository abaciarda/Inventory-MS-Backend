package com.inventoryms.api.service;

import com.inventoryms.api.dto.profitability.CategoryProfitability;
import com.inventoryms.api.dto.profitability.ProductProfitability;
import com.inventoryms.api.dto.profitability.ProfitabilitySummary;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfitabilityService {

    private final ProductRepository productRepository;

    public ProfitabilityService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProfitabilitySummary getSummary() {
        List<Product> products = productRepository.findAll();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;

        for (Product p : products) {
            int qty = p.getStock() != null ? p.getStock().getCurrentQuantity() : 0;
            if (qty > 0) {
                totalCost = totalCost.add(p.getCostPrice().multiply(BigDecimal.valueOf(qty)));
                totalRetail = totalRetail.add(p.getSalesPrice().multiply(BigDecimal.valueOf(qty)));
            }
        }

        BigDecimal potentialProfit = totalRetail.subtract(totalCost);
        BigDecimal margin = BigDecimal.ZERO;
        if (totalRetail.compareTo(BigDecimal.ZERO) > 0) {
            margin = potentialProfit.divide(totalRetail, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        return new ProfitabilitySummary(totalCost, totalRetail, potentialProfit, margin);
    }

    public List<CategoryProfitability> getCategoryProfitability() {
        List<Product> products = productRepository.findAll();
        Map<String, CategoryProfitability> map = new HashMap<>();

        for (Product p : products) {
            if (p.getCategory() == null) continue;
            String catName = p.getCategory().getName();
            int qty = p.getStock() != null ? p.getStock().getCurrentQuantity() : 0;
            
            BigDecimal cost = p.getCostPrice().multiply(BigDecimal.valueOf(qty));
            BigDecimal retail = p.getSalesPrice().multiply(BigDecimal.valueOf(qty));
            BigDecimal profit = retail.subtract(cost);

            CategoryProfitability cp = map.getOrDefault(catName, new CategoryProfitability(catName, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            cp.setCostValue(cp.getCostValue().add(cost));
            cp.setRetailValue(cp.getRetailValue().add(retail));
            cp.setPotentialProfit(cp.getPotentialProfit().add(profit));
            map.put(catName, cp);
        }

        for (CategoryProfitability cp : map.values()) {
            if (cp.getRetailValue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal margin = cp.getPotentialProfit().divide(cp.getRetailValue(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                cp.setProfitMargin(margin);
            }
        }

        return new ArrayList<>(map.values());
    }

    public List<ProductProfitability> getTopProductsByProfit() {
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .filter(p -> p.getStock() != null && p.getStock().getCurrentQuantity() > 0)
                .map(p -> {
                    int qty = p.getStock().getCurrentQuantity();
                    BigDecimal cost = p.getCostPrice().multiply(BigDecimal.valueOf(qty));
                    BigDecimal retail = p.getSalesPrice().multiply(BigDecimal.valueOf(qty));
                    BigDecimal profit = retail.subtract(cost);
                    BigDecimal margin = BigDecimal.ZERO;
                    if (retail.compareTo(BigDecimal.ZERO) > 0) {
                        margin = profit.divide(retail, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                    }
                    return new ProductProfitability(p.getId(), p.getName(), p.getSku(), p.getCostPrice(), p.getSalesPrice(), qty, profit, margin);
                })
                .sorted((a, b) -> b.getPotentialProfit().compareTo(a.getPotentialProfit()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
