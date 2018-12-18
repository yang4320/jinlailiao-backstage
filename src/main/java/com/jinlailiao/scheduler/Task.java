package com.jinlailiao.scheduler;

import com.jinlailiao.bean.VoteForbidden;
import com.jinlailiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * CRON表达式 含义
 * 
 * "0 0 12 * * ?" 每天中午十二点触发
 * 
 * "0 15 10 ? * *" 每天早上10：15触发
 * 
 * "0 15 10 * * ?" 每天早上10：15触发
 * 
 * "0 15 10 * * ? *" 每天早上10：15触发
 * 
 * "0 15 10 * * ? 2005" 2005年的每天早上10：15触发
 * 
 * "0 * 14 * * ?" 每天从下午2点开始到2点59分每分钟一次触发
 * 
 * "0 0/5 14 * * ?" 每天从下午2点开始到2：55分结束每5分钟一次触发
 * 
 * "0 0/5 14,18 * * ?" 每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
 * 
 * "0 0-5 14 * * ?" 每天14:00至14:05每分钟一次触发
 * 
 * "0 10,44 14 ? 3 WED" 三月的每周三的14：10和14：44触发
 * 
 * "0 15 10 ? * MON-FRI" 每个周一、周二、周三、周四、周五的10：15触发
 * 
 *
 */
@Component("task")
public class Task {

	@Resource
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	/**
     * 每天凌晨检查禁言到期人员，并重置禁言状态
     */
    @Scheduled(cron = "0 0 3 ? * *")
//    @Scheduled(cron = "0 9 22 ? * *")
    public void doTaskReduceVoteForbidden() {
		logger.info("查询禁言到期的人员，并重置状态");
		List<VoteForbidden> voteForbiddenList = userService.getVoteForbiddenToReduce();
		voteForbiddenList.forEach(voteForbidden -> {
			logger.info("重置openid:{}人员的禁言状态",voteForbidden.getOpenId());
			userService.reduceTo0VoteForbidden(voteForbidden.getOpenId());
		});
	}
    
}
