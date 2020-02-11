package com.creactiviti.piper.taskhandler.io;

import java.util.List;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * @author Arik Cohen
 * @since Feb, 11 2020
 */
@Component("io/ls")
public class Ls implements TaskHandler<List<String>> {

  @Override
  public List<String> handle (Task aTask) throws Exception {
    
    return null;
  }

}
