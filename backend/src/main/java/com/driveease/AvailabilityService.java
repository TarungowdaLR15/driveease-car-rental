package com.driveease;
import java.sql.Timestamp; import java.time.Instant; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Service;
@Service public class AvailabilityService {
  private final JdbcTemplate jdbc; public AvailabilityService(JdbcTemplate j){jdbc=j;}
  public boolean isFree(long vehicleId, Instant from, Instant to){
    Integer n=jdbc.queryForObject("""
      SELECT count(*) FROM bookings WHERE vehicle_id=? AND status IN('PENDING','CONFIRMED','ACTIVE','OVERDUE')
      AND tstzrange(pickup_at,return_at,'[)') && tstzrange(?,?,'[)')""",Integer.class,vehicleId,Timestamp.from(from),Timestamp.from(to));
    return n!=null && n==0;
  }
}
