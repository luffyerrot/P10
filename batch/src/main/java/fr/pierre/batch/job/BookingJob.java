package fr.pierre.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.pierre.batch.entities.Booking;
import fr.pierre.batch.entities.RequestAndCopy;
import fr.pierre.batch.processor.BookingProcessor;
import fr.pierre.batch.processor.RequestProcessor;
import fr.pierre.batch.reader.BookingReader;
import fr.pierre.batch.reader.RequestReader;
import fr.pierre.batch.util.Token;
import fr.pierre.batch.writer.BookingWriter;
import fr.pierre.batch.writer.RequestWriter;

@Configuration
@EnableBatchProcessing
public class BookingJob {

	@Autowired
  	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job importUserJob() {
		new Token();
	  return this.jobBuilderFactory.get("importUserJob")
			  .start(step1())
			  .next(step2())
			  .build();
	}

	@Bean
	public Step step1() {
	  return this.stepBuilderFactory.get("step1")
	    .<Booking, Booking> chunk(10)
	    .reader(new BookingReader())
	    .processor(new BookingProcessor())
	    .writer(new BookingWriter())
	    .build();
	}
	
	@Bean
	public Step step2() {
	  return this.stepBuilderFactory.get("step2")
	    .<RequestAndCopy, Long> chunk(10)
	    .reader(new RequestReader())
	    .processor(new RequestProcessor())
	    .writer(new RequestWriter())
	    .build();
	}
}
