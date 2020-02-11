package com.creactiviti.piper.taskhandler.io;

import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * @author Arik Cohen
 * @since Feb, 11 2020
 */
@Component("io/ls")
class Ls implements TaskHandler<List<Ls.FileInfo>> {

  @Override
  public List<Ls.FileInfo> handle (Task aTask) throws Exception {
    String path = aTask.getRequiredString("path");
    String glob = aTask.getString("glob","*.*");
    boolean resursive = aTask.getBoolean("recursive", false);
    return doLs (path, glob, resursive);
  }
  
  private List<Ls.FileInfo> doLs (String aPath, String aGlob, boolean aRecursive) throws Exception {
    Path dir = Paths.get(aPath);
    
    PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:"+aGlob);

    List<FileInfo> result = new ArrayList<>();

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path file : stream) {
        if(Files.isDirectory(file) && aRecursive) {
          result.addAll(doLs(file.toString(), aGlob, aRecursive));
        }
        else {
          if(pathMatcher.matches(file.getFileName())) {
            result.add(new FileInfo(file));
          }
        }
      }
    }

    return result;
  }

  static class FileInfo {

    private final Path path;
    
    FileInfo (Path aPath) {
      path = aPath;
    }

    public String getFileName () {
      return path.getFileName().toString();
    }
    
    public String getFullPath () {
      return path.toString();
    }

  }

}
