package org.dice_research.raki.verbalizer.pipeline.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RAKIPythonBridge {

  protected static final Logger LOG = LogManager.getLogger(RAKIPythonBridge.class);

  private String scriptPath = null;
  private String arguments = null;

  public String run() throws ExecuteException, IOException {

    final CommandLine cmdLine = CommandLine.parse(getCommandLine());

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

    final DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    final int exitCode = executor.execute(cmdLine);
    if (exitCode == 0) {
      final String rtn = outputStream.toString().trim();

      LOG.debug("Should contain script output:\n " + rtn);

      outputStream.close();

      return rtn;
    } else {
      LOG.error("Errors be detected " + exitCode);
      return null;
    }
  }

  //
  protected String getCommandLine() {
    return "python3 " + getScriptPath() + " " + getArguments();
  }

  public RAKIPythonBridge setScriptPath(final String scriptPath) {
    this.scriptPath = scriptPath;
    return this;
  }

  public String getScriptPath() {
    return scriptPath;
  }

  public RAKIPythonBridge setArguments(final String arguments) {
    this.arguments = arguments;
    return this;
  }

  public String getArguments() {
    return arguments;
  }
}
