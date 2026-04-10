package com.braininventory.monitoring.common.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing productivity metrics for an agent.
 * Keeps persistence concerns separate from API responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductivityReportDto {

    private String agentId;
    private long idleTime;
    private long productiveAppTime;
    private long productiveWebTime;
    private double productivityScore;

    /**
     * Calculate productivity score based on current values.
     * Formula:
     *   (productiveAppTime + productiveWebTime) /
     *   (idleTime + productiveAppTime + productiveWebTime) * 100
     */
    public void calculateScore() {
        long denominator = idleTime + productiveAppTime + productiveWebTime;
        if (denominator > 0) {
            this.productivityScore =
                    ((double) (productiveAppTime + productiveWebTime) / denominator) * 100;
        } else {
            this.productivityScore = 0.0;
        }
    }
}