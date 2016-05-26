package io.github.luohong.syslog;

import io.github.luohong.syslog.bean.SysLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class SysLogService implements Runnable{

	private static final Log log = Logs.getLog(SysLogService.class);
	
	ExecutorService es;
	
	LinkedBlockingQueue<SysLog> queue;
	
	public static final  SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	
	@Inject
	protected Dao dao;
	
	@Override
	public void run() {
		while (true) {
			LinkedBlockingQueue<SysLog> queue = this.queue;
			if(queue == null)
				break;
			try {
				SysLog sysLog = queue.poll(1, TimeUnit.SECONDS);
				if (sysLog != null) {
					sync(sysLog);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * 异步存储日记
	 * @param sysLog
	 */
	public void async(SysLog sysLog) {
		LinkedBlockingQueue<SysLog> queue = this.queue;
		if (queue != null) {
			try {
				boolean re = queue.offer(sysLog, 1,TimeUnit.SECONDS);
				if (!re) {
					log.info("syslog queue is full, drop it ...");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 同步存储日记
	 * @param sysLog
	 */
	public void sync(SysLog sysLog) {
		try {
			Dao dao = Daos.ext(this.dao, ((DateFormat)ym.clone()).format(sysLog.getCreateTime()));
			dao.fastInsert(sysLog);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void init() {
		queue = new LinkedBlockingQueue<>();
		int c = Runtime.getRuntime().availableProcessors();
		es = Executors.newFixedThreadPool(c);
		for (int i = 0; i < c; i++) {
			es.submit(this);
		}
	}
	
	public void close() throws InterruptedException {
		queue = null;
		if (es != null && !es.isShutdown()) {
			es.shutdown();
			es.awaitTermination(5, TimeUnit.SECONDS);	
		}
	}

}
