package com.example.chulift.demoapplication.config;


public class Config {
    public static String serverUrl = "http://158.108.207.4/";
    public static String projectName = "sp_ScoringSystem/";

    public static int minKeyboardInput = 1;
    public static int maxKeyboardInput = 200;
    public static int maxChoiceOfTemplate = 5;
    public static int minChoiceOfTemplate = 3;

    public static void setServerUrl(String serverUrl) {
        Config.serverUrl = serverUrl;
    }

    public static void setProjectName(String projectName) {
        Config.projectName = projectName;
    }
}
