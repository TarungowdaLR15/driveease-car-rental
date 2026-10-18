package com.driveease;
import static org.junit.jupiter.api.Assertions.*; import java.math.BigDecimal; import java.time.Instant; import org.junit.jupiter.api.Test;
class PricingServiceTest {
  PricingService s=new PricingService(); Instant t=Instant.parse("2026-10-20T10:00:00Z");
  @Test void partialDayChargesFullDay(){ var q=s.quote(new BigDecimal("3200"),t,t.plusSeconds(25*3600),BigDecimal.ZERO); assertEquals(2,q.days()); }
  @Test void goldDiscountAndGst(){ var q=s.quote(new BigDecimal("1000"),t,t.plusSeconds(86400),new BigDecimal("10")); assertEquals(new BigDecimal("1062.00"),q.total()); }
  @Test void depositNotInTotal(){ var q=s.quote(new BigDecimal("1000"),t,t.plusSeconds(86400),BigDecimal.ZERO); assertEquals(new BigDecimal("5000.00"),q.deposit()); }
  @Test void rejectsBadDates(){ assertThrows(IllegalArgumentException.class,()->s.quote(BigDecimal.TEN,t,t,BigDecimal.ZERO)); }
}
