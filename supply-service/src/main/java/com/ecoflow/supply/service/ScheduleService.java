package com.ecoflow.supply.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.supply.dto.ScheduleRequest;
import com.ecoflow.supply.dto.SupplyPlan;
import com.ecoflow.supply.model.DistributionSchedule;
import com.ecoflow.supply.model.SupplyZone;
import com.ecoflow.supply.repository.DistributionScheduleRepository;

// Water Distribution Management: planning supply cycles.
@Service
@Transactional
public class ScheduleService {

	private final DistributionScheduleRepository schedules;
	private final ZoneService zoneService;

	public ScheduleService(DistributionScheduleRepository schedules, ZoneService zoneService) {
		this.schedules = schedules;
		this.zoneService = zoneService;
	}

	/** Newest first: the latest 50 cycles, or the latest 10 for one zone. */
	@Transactional(readOnly = true)
	public List<DistributionSchedule> list(Long zoneId) {
		if (zoneId == null) {
			return schedules.findTop50ByOrderByIdDesc();
		}
		return schedules.findTop10ByZone_IdOrderByIdDesc(zoneId);
	}

	/**
	 * When zone source -> not NORMAL,  cycle may not exceed the share of demand.
	 *  plan of next cycle is retrieved from water-service using ZoneService.
	 */
	public DistributionSchedule create(ScheduleRequest request) {
		SupplyZone zone = zoneService.get(request.zoneId());
		if (!request.endTime().isAfter(request.startTime())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time must be after start time");
		}
		SupplyPlan plan = zoneService.plan(zone.getId());
		if (!"NORMAL".equals(plan.availability()) && request.allocatedMl() > plan.suggestedMl()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, plan.rule() + ", so at most "
					+ plan.suggestedMl() + " ML can be allocated to " + zone.getName() + " (asked: " + request.allocatedMl() + " ML)");
		}
		return schedules.save(new DistributionSchedule(zone, request.supplyDate(), request.startTime(),
				request.endTime(), request.allocatedMl()));
	}

}
