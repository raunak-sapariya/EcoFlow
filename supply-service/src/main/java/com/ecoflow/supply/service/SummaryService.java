package com.ecoflow.supply.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecoflow.supply.dto.DailySupply;
import com.ecoflow.supply.dto.SupplySummary;
import com.ecoflow.supply.model.DistributionSchedule;
import com.ecoflow.supply.model.SupplyZone;
import com.ecoflow.supply.model.UsageRecord;
import com.ecoflow.supply.repository.DistributionScheduleRepository;
import com.ecoflow.supply.repository.UsageRecordRepository;

@Service
@Transactional(readOnly = true)
public class SummaryService {

	private final ZoneService zoneService;
	private final DistributionScheduleRepository schedules;
	private final UsageRecordRepository usage;

	public SummaryService(ZoneService zoneService, DistributionScheduleRepository schedules, UsageRecordRepository usage) {
		this.zoneService = zoneService;
		this.schedules = schedules;
		this.usage = usage;
	}

	public SupplySummary summary() {
		LocalDate today = LocalDate.now();

		int zoneCount = 0;
		long population = 0;
		double demand = 0;
		for (SupplyZone z : zoneService.list()) {
			zoneCount++;
			population += z.getPopulation();
			demand += z.getDailyDemandMl();
		}

	
		double allocatedToday = 0;
		long cyclesToday = 0;
		for (DistributionSchedule s : schedules.findBySupplyDate(today)) {
			if (!"CANCELLED".equals(s.getStatus())) {
				allocatedToday += s.getAllocatedMl();
				cyclesToday++;
			}
		}

		LocalDate lastMonth = today.withDayOfMonth(1).minusMonths(1);
		Map<String, Double> usageByCategory = new LinkedHashMap<>();
		usageByCategory.put("DOMESTIC", 0.0);
		usageByCategory.put("INDUSTRIAL", 0.0);
		usageByCategory.put("INSTITUTIONAL", 0.0);
		usageByCategory.put("AGRICULTURAL", 0.0);
		for (UsageRecord r : usage.findByUsageMonth(lastMonth)) {
			usageByCategory.put(r.getCategory(), round1(usageByCategory.get(r.getCategory()) + r.getVolumeMl()));
		}

		List<DistributionSchedule> recent = schedules.findBySupplyDateBetween(today.minusDays(6), today);
		List<DailySupply> lastSevenDays = new ArrayList<>();
		for (int daysBack = 6; daysBack >= 0; daysBack--) {
			LocalDate day = today.minusDays(daysBack);
			double planned = 0;
			double supplied = 0;
			for (DistributionSchedule s : recent) {
				if (s.getSupplyDate().equals(day) && !"CANCELLED".equals(s.getStatus())) {
					planned += s.getAllocatedMl();
					if (s.getSuppliedMl() != null) {
						supplied += s.getSuppliedMl();
					}
				}
			}
			lastSevenDays.add(new DailySupply(day, round1(planned), round1(supplied)));
		}

		return new SupplySummary(zoneCount, population, round1(demand), round1(allocatedToday), cyclesToday,
				lastMonth, usageByCategory, lastSevenDays);
	}

	private static double round1(double v) {
		return Math.round(v * 10) / 10.0;
	}

}
