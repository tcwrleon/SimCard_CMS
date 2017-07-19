package com.sunyard.service.system;

import com.sunyard.entity.system.RunLogEntity;
import com.sunyard.service.ReportService;

public interface RunLogService extends ReportService<RunLogEntity> {
	public void add(RunLogEntity t) throws Exception;
}
