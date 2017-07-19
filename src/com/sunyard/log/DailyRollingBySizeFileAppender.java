////////////////////////////////////////////////////////////////////////////
// 
// Copyright (C) 2008 e-Channels CORPORATION
// 
// ALL RIGHTS RESERVED BY e-Channels CORPORATION, THIS PROGRAM
// MUST BE USED SOLELY FOR THE PURPOSE FOR WHICH IT WAS  
// FURNISHED BY e-Channels CORPORATION, NO PART OF THIS PROGRAM
// MAY BE REPRODUCED OR DISCLOSED TO OTHERS, IN ANY FORM
// WITHOUT THE PRIOR WRITTEN PERMISSION OF e-Channels CORPORATION.
// USE OF COPYRIGHT NOTICE DOES NOT EVIDENCE PUBLICATION
// OF THE PROGRAM
// 
//			e-Channels CONFIDENTIAL AND PROPRIETARY
// 
////////////////////////////////////////////////////////////////////////////
package com.sunyard.log;

/**
 *按日期及大小写日志文件实现类
 * 
 */

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 同时支持按时间和大小分割日志文件,同一天文件超过一定大小(maxFileSize)时,会自动拆分一天中的文件,
 * 同一天中最多保留maxBackupIndex份文件
 * 
 */

public class DailyRollingBySizeFileAppender extends FileAppender {

	// The code assumes that the following constants are in a increasing
	// sequence.
	static final int TOP_OF_TROUBLE = -1;

	static final int TOP_OF_MINUTE = 0;

	static final int TOP_OF_HOUR = 1;

	static final int HALF_DAY = 2;

	static final int TOP_OF_DAY = 3;

	static final int TOP_OF_WEEK = 4;

	static final int TOP_OF_MONTH = 5;

	protected int maxBackupIndex = 40;

	/**
	 * Set the maximum number of backup files to keep around.
	 * 
	 * <p>
	 * The <b>MaxBackupIndex</b> option determines how many backup files are
	 * kept before the oldest is erased. This option takes a positive integer
	 * value. If set to zero, then there will be no backup files and the log
	 * file will be truncated when it reaches <code>MaxFileSize</code>.
	 */
	public void setMaxBackupIndex(int maxBackups) {
		this.maxBackupIndex = maxBackups;
	}

	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	/**
	 * The default maximum file size is 500MB.
	 */
	protected long maxFileSize = 500 * 1024 * 1024;

	public long getMaximumFileSize() {
		return maxFileSize;
	}

	/**
	 * Set the maximum size that the output file is allowed to reach before
	 * being rolled over to backup files.
	 * 
	 * <p>
	 * This method is equivalent to {@link #setMaxFileSize} except that it is
	 * required for differentiating the setter taking a <code>long</code>
	 * argument from the setter taking a <code>String</code> argument by the
	 * JavaBeans {@link java.beans.Introspector Introspector}.
	 * 
	 * @see #setMaxFileSize(String)
	 */
	public void setMaximumFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * Set the maximum size that the output file is allowed to reach before
	 * being rolled over to backup files.
	 * 
	 * <p>
	 * In configuration files, the <b>MaxFileSize</b> option takes an long
	 * integer in the range 0 - 2^63. You can specify the value with the
	 * suffixes "KB", "MB" or "GB" so that the integer is interpreted being
	 * expressed respectively in kilobytes, megabytes or gigabytes. For example,
	 * the value "10KB" will be interpreted as 10240.
	 */
	public void setMaxFileSize(String value) {
		maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
	}

	protected void setQWForFiles(Writer writer) {
		this.qw = new CountingQuietWriter(writer, errorHandler);
	}

	/**
	 * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd"
	 * meaning daily rollover.
	 */
	private String datePattern = "'.'yyyy-MM-dd";

	/**
	 * The log file will be renamed to the value of the scheduledFilename
	 * variable when the next interval is entered. For example, if the rollover
	 * period is one hour, the log file will be renamed to the value of
	 * "scheduledFilename" at the beginning of the next hour.
	 * 
	 * The precise time when a rollover occurs depends on logging activity.
	 */
	private String scheduledFilename;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	Date now = new Date();

	SimpleDateFormat sdf;

	LianaRollingCalendar rc = new LianaRollingCalendar();

	int checkPeriod = TOP_OF_TROUBLE;

	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	/**
	 * The default constructor does nothing.
	 */
	public DailyRollingBySizeFileAppender() {
	}

	/**
	 * Instantiate a <code>DailyRollingBySizeFileAppender</code> and open the
	 * file designated by <code>filename</code>. The opened filename will become
	 * the ouput destination for this appender.
	 */
	public DailyRollingBySizeFileAppender(Layout layout, String filename, String datePattern) throws IOException {
		super(layout, filename, true);
		this.datePattern = datePattern;
		activateOptions();
	}

	/**
	 * The <b>DatePattern</b> takes a string in the same format as expected by
	 * {@link SimpleDateFormat}. This options determines the rollover schedule.
	 */
	public void setDatePattern(String pattern) {
		datePattern = pattern;
	}

	/** Returns the value of the <b>DatePattern</b> option. */
	public String getDatePattern() {
		return datePattern;
	}

	public void activateOptions() {
		super.activateOptions();
		if (datePattern != null && fileName != null) {
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(datePattern);
			int type = computeCheckPeriod();
			printPeriodicity(type);
			rc.setType(type);
			File file = new File(fileName);
			scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));

		} else {
			LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
		}
	}

	void printPeriodicity(int type) {
		switch (type) {
		case TOP_OF_MINUTE:
			LogLog.debug("Appender [" + name + "] to be rolled every minute.");
			break;
		case TOP_OF_HOUR:
			LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
			break;
		case HALF_DAY:
			LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");
			break;
		case TOP_OF_DAY:
			LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
			break;
		case TOP_OF_WEEK:
			LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
			break;
		case TOP_OF_MONTH:
			LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
			break;
		default:
			LogLog.warn("Unknown periodicity for appender [" + name + "].");
		}
	}

	// This method computes the roll over period by looping over the
	// periods, starting with the shortest, and stopping when the r0 is
	// different from from r1, where r0 is the epoch formatted according
	// the datePattern (supplied by the user) and r1 is the
	// epoch+nextMillis(i) formatted according to datePattern. All date
	// formatting is done in GMT and not local format because the test
	// logic is based on comparisons relative to 1970-01-01 00:00:00
	// GMT (the epoch).

	int computeCheckPeriod() {
		LianaRollingCalendar rollingCalendar = new LianaRollingCalendar(gmtTimeZone, Locale.ENGLISH);
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null) {
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
															// formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);
				if (r0 != null && r1 != null && !r0.equals(r1)) {
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE; // Deliberately head for trouble...
	}

	/**
	 * Rollover the current file to a new file.
	 */
	void rollOverByDate() throws IOException {

		/* Compute filename, but only if datePattern is specified */
		if (datePattern == null) {
			errorHandler.error("Missing DatePattern option in rollOver().");
			return;
		}

		String datedFilename = fileName + sdf.format(now);
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledFilename.equals(datedFilename)) {
			return;
		}

		// close current file, and rename it to datedFilename
		this.closeFile();

		File target = new File(scheduledFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		boolean result = file.renameTo(target);
		if (result) {
			LogLog.debug(fileName + " -> " + scheduledFilename);
		} else {
			LogLog.error("Failed to rename [" + fileName + "] to [" + scheduledFilename + "].");
		}

		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
			errorHandler.error("setFile(" + fileName + ", false) call failed.");
		}
		scheduledFilename = datedFilename;
	}

	private void rollOverBySize() {

		LogLog.debug("rolling over count=" + ((CountingQuietWriter) qw).getCount());
		LogLog.debug("maxBackupIndex=" + maxBackupIndex);

		// If maxBackups <= 0, then there is no file renaming to be done.
		if (maxBackupIndex > 0) {
			File target;
			String datedFilename = fileName + sdf.format(now);

			// Delete the oldest file, to keep Windows happy.
			File file = new File(datedFilename + '.' + maxBackupIndex);
			if (file.exists())
				file.delete();

			// Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3,
			// 2}
			for (int i = maxBackupIndex - 1; i >= 1; i--) {
				file = new File(datedFilename + '.' + i);
				if (file.exists()) {
					target = new File(datedFilename + '.' + (i + 1));
					LogLog.debug("Renaming file " + file + " to " + target);
					file.renameTo(target);
				}
			}

			// Rename fileName to fileName.1
			target = new File(datedFilename + '.' + 1);

			this.closeFile(); // keep windows happy.

			file = new File(fileName);
			LogLog.debug("Renaming file " + file + " to " + target);
			file.renameTo(target);
		}

		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(fileName, false, bufferedIO, bufferSize);
		} catch (IOException e) {
			LogLog.error("setFile(" + fileName + ", false) call failed.", e);
		}
	}

	/**
	 * This method differentiates DailyRollingBySizeFileAppender from its super
	 * class.
	 * 
	 * <p>
	 * Before actually logging, this method will check whether it is time to do
	 * a rollover. If it is, it will schedule the next rollover time and then
	 * rollover.
	 * */
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
			try {
				rollOverByDate();
			} catch (IOException ioe) {
				LogLog.error("rollOver() failed.", ioe);
			}
		}
		super.subAppend(event);

		// 按日期分割同时检查文件大小分割,同一天超过限制的文件需要加后缀切割文件
		if ((fileName != null) && ((CountingQuietWriter) qw).getCount() >= maxFileSize) {
			rollOverBySize();
		}
	}
}


