package com.driveease;
import java.math.*; import java.time.*; import org.springframework.stereotype.Service;
/** Each started 24h period = 1 rental day. Money is BigDecimal, rounded HALF_UP to 2 dp. */
@Service public class PricingService {
  public record Quote(long days, BigDecimal base, BigDecimal discount, BigDecimal tax, BigDecimal total, BigDecimal deposit){}
  static final BigDecimal GST=new BigDecimal("0.18"), MIN_DEPOSIT=new BigDecimal("5000");
  public Quote quote(BigDecimal daily, Instant pickup, Instant ret, BigDecimal tierPct){
    if(!ret.isAfter(pickup)) throw new IllegalArgumentException("Return must be after pickup");
    long mins=Duration.between(pickup,ret).toMinutes(), days=(mins+1439)/1440;
    BigDecimal base=daily.multiply(BigDecimal.valueOf(days));
    BigDecimal disc=base.multiply(tierPct).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
    BigDecimal sub=base.subtract(disc), tax=sub.multiply(GST).setScale(2,RoundingMode.HALF_UP);
    BigDecimal dep=daily.multiply(BigDecimal.TWO).max(MIN_DEPOSIT);
    return new Quote(days,base.setScale(2),disc,tax,sub.add(tax),dep.setScale(2)); // deposit kept apart from total
  }
}
