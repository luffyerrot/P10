package fr.pierre.batch;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BatchApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;
	
	private AtomicInteger batchRunCounter = new AtomicInteger(0);
	
	public static void main(String[] args) {
        
		SpringApplication.run(BatchApplication.class, args);
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void run() throws Exception {
		String dateParam = new Date().toString();
		JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
		jobLauncher.run(job, param);
		batchRunCounter.incrementAndGet();
	}
}
