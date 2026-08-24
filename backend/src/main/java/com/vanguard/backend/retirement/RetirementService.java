package com.vanguard.backend.retirement;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RetirementService {

    public RetirementResponse calculateRetirement(RetirementRequest request) {
        List<RetirementResponse.YearlyProjection> projections = new ArrayList<>();
        
        int yearsToRetirement = request.getRetirementAge() - request.getCurrentAge();
        double currentBalance = request.getCurrentSavings();
        double monthlyRate = request.getExpectedAnnualReturn() / 12;
        double annualContribution = request.getMonthlyContribution() * 12;

        projections.add(new RetirementResponse.YearlyProjection(request.getCurrentAge(), currentBalance));

        for (int i = 1; i <= yearsToRetirement; i++) {
            // Simple compound interest with annual contributions added at end of year
            currentBalance = currentBalance * (1 + request.getExpectedAnnualReturn()) + annualContribution;
            projections.add(new RetirementResponse.YearlyProjection(request.getCurrentAge() + i, currentBalance));
        }

        return new RetirementResponse(currentBalance, projections);
    }
}
