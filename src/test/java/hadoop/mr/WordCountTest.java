package hadoop.mr;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordCountTest {

  @Test
  public void runMR() throws Exception {
    String input = "/Users/pankajthakur/Documents/hadoop-2.8.0/input";
    String output = "/Users/pankajthakur/Documents/hadoop-2.8.0/output";
    new WordCount().runMR(input, output);
  }

}