package com.driveease;
import java.math.BigDecimal; import java.time.Instant; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
@RestController @RequestMapping("/api/v1") @CrossOrigin(origins="${CORS_ORIGIN:http://localhost:5173}")
public class QuoteController {
  record QuoteRequest(@NotNull Long vehicleId,@NotNull Instant pickup,@NotNull Instant ret,String tier){}
  private final PricingService pricing; private final AvailabilityService avail; private final JdbcTemplate jdbc;
  QuoteController(PricingService p,AvailabilityService a,JdbcTemplate j){pricing=p;avail=a;jdbc=j;}
  @PostMapping("/bookings/quote")
  public Object quote(@RequestBody @jakarta.validation.Valid QuoteRequest r){
    BigDecimal daily=jdbc.queryForObject("SELECT daily_price FROM vehicles WHERE id=? AND active",BigDecimal.class,r.vehicleId());
    BigDecimal pct=jdbc.queryForObject("SELECT discount_pct FROM pricing_tiers WHERE code=?",BigDecimal.class,r.tier()==null?"STANDARD":r.tier().toUpperCase());
    return java.util.Map.of("available",avail.isFree(r.vehicleId(),r.pickup(),r.ret()),"quote",pricing.quote(daily,r.pickup(),r.ret(),pct));
  }
}
