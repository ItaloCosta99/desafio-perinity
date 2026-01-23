package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyRevenueStats(
    List<MonthlyRecord> monthlyRecords,
    BigDecimal totalRevenue,
    BigDecimal totalTax,
    BigDecimal totalFinal) {
  public record MonthlyRecord(
      String month,
      BigDecimal revenue,
      BigDecimal tax,
      BigDecimal finalAmount) {
  }
}