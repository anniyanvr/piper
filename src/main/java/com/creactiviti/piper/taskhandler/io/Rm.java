package com.creactiviti.piper.taskhandler.io;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
 * 
 * <p>The difference between File.delete() and this method are:
 * 
 * <p>A directory to be deleted does not have to be empty.
 * 
 * <p>No exceptions are thrown when a file or directory cannot be deleted.
 * 
 * @author Arik Cohen
 * @since Feb, 11 2020
 */
@Component("io/rm")
class Rm implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) throws Exception {
    FileUtils.deleteQuietly(new File (aTask.getRequiredString("path")));
    return null;
  }

}
