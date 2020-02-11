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

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * Writes a string to a file creating the file if it does 
 * not exist using the default encoding for the VM.
 * 
 * @author Arik Cohen
 * @since Feb, 10 2020
 */
@Component
class WriteStringToFile implements TaskHandler<Object> {

  @Override 
  public Object handle (Task aTask) throws IOException {
    String string = aTask.getRequiredString("string");
    String filePath = aTask.getRequiredString("filePath");
    FileUtils.writeStringToFile(new File(filePath), string);
    return null;
  }

}
