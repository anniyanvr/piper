/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.creactiviti.piper.taskhandler.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * Simple worker which performs the download of a file (given its URL).
 *
 * @since Sep 06, 2018
 */
@Component("io/download")
class Download implements TaskHandler<Object> {
  
  @Override
  public Object handle (Task aTask) throws MalformedURLException, IOException {
    
    FileUtils.copyURLToFile(
      new URL(aTask.getRequiredString("url")), 
      new File(aTask.getRequiredString("filepath"))
    );
   
    return null;
   
  }

}
