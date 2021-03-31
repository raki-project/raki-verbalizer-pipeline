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

  public static void main(final String[] args) {
    try {
      test();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static String resolvePythonScriptPath(final String script) {
    // TODO: add path
    return script;
  }

  public static void test() throws ExecuteException, IOException {
    final String line = "python " + resolvePythonScriptPath("hello.py");
    final CommandLine cmdLine = CommandLine.parse(line);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

    final DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    final int exitCode = executor.execute(cmdLine);
    LOG.info("No errors should be detected " + exitCode);
    LOG.info("Should contain script output: " + outputStream.toString().trim());
  }
}
