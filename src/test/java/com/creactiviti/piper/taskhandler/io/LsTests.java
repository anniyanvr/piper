package com.creactiviti.piper.taskhandler.io;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.creactiviti.piper.core.task.SimpleTaskExecution;

public class LsTests {

  @Test
  public void test1 () throws Exception {
    
   Ls ls = new Ls();
   
   ClassPathResource rsc = new ClassPathResource("ls-test-folder");
   
   SimpleTaskExecution task = new SimpleTaskExecution();
   
   task.set("path", rsc.getFile().getAbsolutePath());
   
   List<Ls.FileInfo> results = ls.handle(task);

   Set<String> actual = results.stream()
                                .map(fi->fi.getFileName())
                                .collect(Collectors.toSet());
   
   Assertions.assertEquals(Set.of("a.txt","b.txt","d.html"), actual);
    
  }
  
  @Test
  public void test2 () throws Exception {
    
   Ls ls = new Ls();
   
   ClassPathResource rsc = new ClassPathResource("ls-test-folder");
   
   SimpleTaskExecution task = new SimpleTaskExecution();
   
   task.set("path", rsc.getFile().getAbsolutePath());
   task.set("glob", "*.txt");
   
   List<Ls.FileInfo> results = ls.handle(task);

   Set<String> actual = results.stream()
                                .map(fi->fi.getFileName())
                                .collect(Collectors.toSet());
   
   Assertions.assertEquals(Set.of("a.txt","b.txt"), actual);
    
  }
  
  @Test
  public void test3 () throws Exception {
    
   Ls ls = new Ls();
   
   ClassPathResource rsc = new ClassPathResource("ls-test-folder");
   
   SimpleTaskExecution task = new SimpleTaskExecution();
   
   task.set("path", rsc.getFile().getAbsolutePath());
   task.set("glob", "*.txt");
   task.set("recursive", true);
   
   List<Ls.FileInfo> results = ls.handle(task);

   Set<String> actual = results.stream()
                                .map(fi->fi.getFileName())
                                .collect(Collectors.toSet());
   
   Assertions.assertEquals(Set.of("a.txt","b.txt","c.txt"), actual);
    
  }
  
}
