package com.example.demotests;


import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

public class TestSuites {

    @Suite
    @SelectClasses({DemoTest.class})
    @IncludeTags("Task1")
    public static class Suite1 {
    }


    @Suite
    @IncludeTags("Task2")
    public static class Suite2 {
    }


    @Suite
    @SelectClasses({DemoTest.class})
    public static class Suite3 {
    }
}
