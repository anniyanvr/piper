package com.creactiviti.piper.taskhandler.io;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * Creates the directory named by <code>path</code>, including any necessary 
 * but nonexistent parent directories. Note that if this operation fails it 
 * may have succeeded in creating some of the necessary parent directories.
 * 
 * @author Arik Cohen
 * @since Feb, 10 2020
 */
@Component
class Mkdir implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) throws Exception {
    String path = aTask.getRequiredString("path");
    File dir = new File (aTask.getRequiredString("path"));
    boolean success = dir.mkdirs();
    Assert.isTrue(success,"Failed to create directory: " + path);
    return null;
  }

}
