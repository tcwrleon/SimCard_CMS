package com.sunyard.service.system;

import com.sunyard.entity.system.OperLogEntity;
import com.sunyard.service.ReportService;

public interface OperLogService extends ReportService<OperLogEntity>{
	
	public void add(OperLogEntity t) throws Exception;
}
