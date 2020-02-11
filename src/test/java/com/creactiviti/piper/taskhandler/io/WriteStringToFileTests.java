package com.creactiviti.piper.taskhandler.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.creactiviti.piper.core.task.SimpleTaskExecution;
import com.creactiviti.piper.taskhandler.io.WriteStringToFile;

class WriteStringToFileTests {
  
  @Test
  public void test1 () throws IOException {
    File tempFile = File.createTempFile("_temp", ".txt");

    WriteStringToFile handler = new WriteStringToFile();
    SimpleTaskExecution task = SimpleTaskExecution.create();
    task.set("string", "hello world");
    task.set("filePath", tempFile.getAbsolutePath());
    
    handler.handle(task);
    
    String readFileToString = FileUtils.readFileToString(tempFile);
    
    Assertions.assertEquals("hello world", readFileToString);
  }

}
