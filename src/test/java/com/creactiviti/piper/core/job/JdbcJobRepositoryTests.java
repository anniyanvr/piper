package com.creactiviti.piper.core.job;

import static java.time.temporal.ChronoUnit.DAYS;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.task.JdbcTaskExecutionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@SpringBootTest
public class JdbcJobRepositoryTests {

  @Autowired
  private DataSource dataSource; 
  
  @Autowired
  private PlatformTransactionManager txManager;

  private TransactionStatus transaction;
  
  @BeforeEach
  public void before () {
    transaction = txManager.getTransaction(new DefaultTransactionDefinition());
  }
  
  @AfterEach
  public void after () {
    txManager.rollback(transaction);
  }
  
  @Test
  public void test1 () {
    JdbcTaskExecutionRepository taskRepository = new JdbcTaskExecutionRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());
    
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);
    
    int pageTotal = jobRepository.findAll(1).getNumber();
    
    String id = UUID.randomUUID().toString();
    
    SimpleJob job = new SimpleJob();
    job.setPipelineId("demo:1234");
    job.setId(id);
    job.setCreateTime(new Date());
    job.setStatus(JobStatus.CREATED);
    jobRepository.create(job);
    
    Page<Job> all = jobRepository.findAll(1);
    Assertions.assertEquals(pageTotal+1,all.getSize());
    
    Job one = jobRepository.findOne(id);
    Assertions.assertNotNull(one);
  }
  
  @Test
  public void test2 () {
    JdbcTaskExecutionRepository taskRepository = new JdbcTaskExecutionRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());
    
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);
    
    String id = UUID.randomUUID().toString();
    
    SimpleJob job = new SimpleJob();
    job.setId(id);
    job.setPipelineId("demo:1234");
    job.setCreateTime(new Date());
    job.setStatus(JobStatus.CREATED);
    jobRepository.create(job);
    
    Job one = jobRepository.findOne(id);
    
    SimpleJob mjob = new SimpleJob(one);
    mjob.setStatus(JobStatus.FAILED);
    
    // test immutability
    Assertions.assertNotEquals(mjob.getStatus().toString(),one.getStatus().toString());  
    
    jobRepository.merge(mjob);
    one = jobRepository.findOne(id);
    Assertions.assertEquals("FAILED",one.getStatus().toString());  
  }

  @Test
  public void test3 () {
    
    // arrange
    JdbcTaskExecutionRepository taskRepository = new JdbcTaskExecutionRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());

    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);
    
    int todayJobsOriginally = jobRepository.countCompletedJobsToday();

    SimpleJob completedJobYesterday = new SimpleJob();
    completedJobYesterday.setId("1");
    completedJobYesterday.setPipelineId("demo:1234");
    completedJobYesterday.setCreateTime(Date.from(Instant.now().minus(2, DAYS)));
    completedJobYesterday.setStatus(JobStatus.COMPLETED);
    jobRepository.create(completedJobYesterday);
    completedJobYesterday.setEndTime(Date.from(Instant.now().minus(1, DAYS)));
    jobRepository.merge(completedJobYesterday);

    for(int i = 0; i < 5; i++) {
        SimpleJob completedJobToday = new SimpleJob();
        completedJobToday.setId("2."+i);
        completedJobToday.setPipelineId("demo:1234");
        completedJobToday.setCreateTime(Date.from(Instant.now().minus(1, DAYS)));
        completedJobToday.setStatus(JobStatus.COMPLETED);
        jobRepository.create(completedJobToday);
        completedJobToday.setEndTime(new Date());
        jobRepository.merge(completedJobToday);
    }

    SimpleJob runningJobToday = new SimpleJob();
    runningJobToday.setId("3");
    runningJobToday.setPipelineId("demo:1234");
    runningJobToday.setCreateTime(new Date());
    runningJobToday.setStatus(JobStatus.STARTED);
    jobRepository.create(runningJobToday);

    // act
    int todayJobs = jobRepository.countCompletedJobsToday();

    // assert
    Assertions.assertEquals(5+todayJobsOriginally, todayJobs);
  }

  @Test
  public void test4 () {
    // arrange
    JdbcTaskExecutionRepository taskRepository = new JdbcTaskExecutionRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());

    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);

    for(int i = 0; i < 5; i++) {
        SimpleJob completedJobYesterday = new SimpleJob();
        completedJobYesterday.setId(UUID.randomUUID() + "."+i);
        completedJobYesterday.setPipelineId("demo:1234");
        completedJobYesterday.setCreateTime(Date.from(Instant.now().minus(2, DAYS)));
        completedJobYesterday.setStatus(JobStatus.COMPLETED);
        jobRepository.create(completedJobYesterday);
        completedJobYesterday.setEndTime(Date.from(Instant.now().minus(1, DAYS)));
        jobRepository.merge(completedJobYesterday);
    }

    SimpleJob runningJobYesterday = new SimpleJob();
    runningJobYesterday.setId(UUID.randomUUID().toString());
    runningJobYesterday.setPipelineId("demo:1234");
    runningJobYesterday.setCreateTime(Date.from(Instant.now().minus(1, DAYS)));
    runningJobYesterday.setStatus(JobStatus.STARTED);
    jobRepository.create(runningJobYesterday);

    SimpleJob completedJobToday = new SimpleJob();
    completedJobToday.setId(UUID.randomUUID().toString());
    completedJobToday.setPipelineId("demo:1234");
    completedJobToday.setCreateTime(Date.from(Instant.now().minus(1, DAYS)));
    completedJobToday.setStatus(JobStatus.COMPLETED);
    jobRepository.create(completedJobToday);
    completedJobToday.setEndTime(new Date());
    jobRepository.merge(completedJobToday);

    // act
    int yesterdayJobs = jobRepository.countCompletedJobsYesterday();

    // assert
    Assertions.assertEquals(5, yesterdayJobs);
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"));
    return objectMapper;
  }
  
}
