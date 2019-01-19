package com.rackspacecloud.metrics.tenantroutingservice.domain;

public enum RetentionPolicyEnum {

    FIVE_MINUTES ("rp_10d", "10d"){
        @Override
        public String toString() {
            return "5m";
        }
    },
    TWENTY_MINUTES("rp_20d", "20d"){
        @Override
        public String toString() {
            return "20m";
        }

    },
    ONE_HOUR("rp_155d", "155d"){
        @Override
        public String toString() {
            return "60m";
        }

    },
    FOUR_HOURS("rp_300d", "300d"){
        @Override
        public String toString() {
            return "240m";
        }

    },
    ONE_DAY("rp_1825d", "1825d"){
        @Override
        public String toString() {
            return "1440m";
        }

    },
    FULL("rp_5d", "5d"){
        @Override
        public String toString() {
            return "full";
        }

    };
    public String retentionPolicyName;
    public String retentionPolicy;
    public int maxSeriesCount = 10000;
    public String databaseName;
    public String path;

    RetentionPolicyEnum(String policyName, String policy){
        retentionPolicyName = policyName;
        retentionPolicy = policy;
    }

    public abstract String toString();

    public RetentionPolicyEnum setDatabaseName(String dbName) {
        databaseName = dbName;
        return this;
    }

    public RetentionPolicyEnum setPath(String path) {
        this.path = path;
        return this;
    }
}
