package com.creactiviti.piper.plugin.bento4;

import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * mp42hls is a tool that converts an MP4 file to an HLS (HTTP Live Streaming) presentation, 
 * including the generation of the segments and .m3u8 playlist as well as AES-128 and 
 * SAMPLE-AES (for FairPlay) encryption. 
 * 
 * <p>This can be used as a replacement for Apple’s mediafilesegmenter tool.</p>
 * 
 * @author Arik Cohen
 * @since Feb, 11 2020
 * @see https://www.bento4.com/documentation/mp42hls/
 */
@Component("bento4/mp4hls")
class Mp4hls implements TaskHandler<Object> {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (Task aTask) throws Exception {
    List<String> options = aTask.getList("options", String.class);
    CommandLine cmd = new CommandLine ("mp4hls");
    options.forEach(o->cmd.addArgument(o));
    logger.debug("{}", String.join(" ", cmd.toStrings()));
    DefaultExecutor exec = new DefaultExecutor();
    int exitValue = exec.execute(cmd);
    Assert.isTrue(exitValue==0,"Exit value: " + exitValue);
    return null;
  }

}
