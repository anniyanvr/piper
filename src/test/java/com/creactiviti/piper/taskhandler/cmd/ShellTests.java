package com.creactiviti.piper.taskhandler.cmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.creactiviti.piper.core.task.SimpleTaskExecution;

public class ShellTests {

  @Test
  public void test1 () throws Exception {
    Shell shell = new Shell();
    ClassPathResource cpr = new ClassPathResource("schema.sql");
    String output = shell.handle(SimpleTaskExecution.createFrom ("script", "ls -l " + cpr.getFile().getAbsolutePath()));
    Assertions.assertTrue(output.contains("target/classes/schema.sql"));
  }
  
}
