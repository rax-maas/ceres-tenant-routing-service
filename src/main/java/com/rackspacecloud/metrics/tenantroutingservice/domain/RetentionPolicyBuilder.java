package com.rackspacecloud.metrics.tenantroutingservice.domain;

public class RetentionPolicyBuilder {

    private String name;
    private String databaseName;
    private int maxSeriesCount;
    private String retentionPolicyName;
    private String retentionPolicy;


    public RetentionPolicy buildRetentionPolicy(RetentionPolicyEnum retentionPolicy) {
        name = retentionPolicy.toString();
        databaseName = retentionPolicy.databaseName;
        maxSeriesCount = retentionPolicy.maxSeriesCount;
        retentionPolicyName = retentionPolicy.retentionPolicyName;
        this.retentionPolicy = retentionPolicy.retentionPolicy;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }

    public RetentionPolicy buildFullRetentionPolicy(String databaseName) {

        this.databaseName = databaseName;


        RetentionPolicyEnum full = RetentionPolicyEnum.FULL;
        name = full.toString();
        maxSeriesCount = full.maxSeriesCount;
        retentionPolicyName = full.retentionPolicyName;
        this.retentionPolicy = full.retentionPolicy;
        maxSeriesCount = full.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }

    public RetentionPolicy buildFiveMinuteRetentionPolicy(String databaseName) {
        this.databaseName = databaseName;


        RetentionPolicyEnum fiveMinutes = RetentionPolicyEnum.FIVE_MINUTES;
        name = fiveMinutes.toString();
        maxSeriesCount = fiveMinutes.maxSeriesCount;
        retentionPolicyName = fiveMinutes.retentionPolicyName;
        this.retentionPolicy = fiveMinutes.retentionPolicy;
        maxSeriesCount = fiveMinutes.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }
    public RetentionPolicy buildTwentyMinuteRetentionPolicy(String databaseName) {

        this.databaseName = databaseName;


        RetentionPolicyEnum twentyMinutes = RetentionPolicyEnum.TWENTY_MINUTES;
        name = twentyMinutes.toString();
        maxSeriesCount = twentyMinutes.maxSeriesCount;
        retentionPolicyName = twentyMinutes.retentionPolicyName;
        this.retentionPolicy = twentyMinutes.retentionPolicy;
        maxSeriesCount = twentyMinutes.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }

    public RetentionPolicy buildOneHourRetentionPolicy(String databaseName) {

        this.databaseName = databaseName;


        RetentionPolicyEnum oneHour = RetentionPolicyEnum.ONE_HOUR;
        name = oneHour.toString();
        maxSeriesCount = oneHour.maxSeriesCount;
        retentionPolicyName = oneHour.retentionPolicyName;
        this.retentionPolicy = oneHour.retentionPolicy;
        maxSeriesCount = oneHour.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }

    public RetentionPolicy buildFourHourRetentionPolicy(String databaseName) {

        this.databaseName = databaseName;


        RetentionPolicyEnum fourHours = RetentionPolicyEnum.FOUR_HOURS;
        name = fourHours.toString();
        maxSeriesCount = fourHours.maxSeriesCount;
        retentionPolicyName = fourHours.retentionPolicyName;
        this.retentionPolicy = fourHours.retentionPolicy;
        maxSeriesCount = fourHours.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }

    public RetentionPolicy buildOneDayRetentionPolicy(String databaseName) {

        this.databaseName = databaseName;


        RetentionPolicyEnum oneDay = RetentionPolicyEnum.ONE_DAY;
        name = oneDay.toString();
        maxSeriesCount = oneDay.maxSeriesCount;
        retentionPolicyName = oneDay.retentionPolicyName;
        this.retentionPolicy = oneDay.retentionPolicy;
        maxSeriesCount = oneDay.maxSeriesCount;

        return new RetentionPolicy(databaseName, retentionPolicyName, name);
    }
}
