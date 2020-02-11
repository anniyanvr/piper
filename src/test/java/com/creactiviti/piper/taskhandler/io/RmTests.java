package com.creactiviti.piper.taskhandler.io;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.creactiviti.piper.core.task.SimpleTaskExecution;
import com.google.common.io.Files;

public class RmTests {

  @Test
  public void test1 () throws Exception {
    Rm rm = new Rm();
    File tempFile = File.createTempFile("_temp", ".txt");
    FileUtils.writeStringToFile(tempFile, "hello world");
    Assertions.assertTrue(tempFile.exists());
    SimpleTaskExecution task = new SimpleTaskExecution ();
    task.set("path", tempFile.getAbsolutePath());
    rm.handle(task);
    Assertions.assertFalse(tempFile.exists());
  }
  
  @Test
  public void test2 () throws Exception {
    Rm rm = new Rm();
    File tempDir = Files.createTempDir();
    Assertions.assertTrue(tempDir.exists());
    SimpleTaskExecution task = new SimpleTaskExecution ();
    task.set("path", tempDir.getAbsolutePath());
    rm.handle(task);
    Assertions.assertFalse(tempDir.exists());
  }
  
}
