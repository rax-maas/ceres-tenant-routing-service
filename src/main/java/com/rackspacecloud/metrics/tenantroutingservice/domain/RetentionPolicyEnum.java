package com.rackspacecloud.metrics.tenantroutingservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public int maxSeriesCount = 100000;
    public String databaseName;

    private RetentionPolicyEnum(String policyName, String policy){
        retentionPolicyName = policyName;
        retentionPolicy = policy;
    }

    public String toString() {
        return this.name();
    }
    public void setDatabaseName(String dbName) {
        databaseName = dbName;
    }
}
