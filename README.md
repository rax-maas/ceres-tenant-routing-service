# Tenant Routing Service
## Responsibilities
- Creates routes for given tenantId and measurement.
- Provides routes for given tenantId and measurement.
- Provides all of the measurements for a given tenantId.

It uses `Redis` database to store all of the route information.

## Routing Metadata
Routing metadata should contain following items:
1. Tenant Id and measurement
1. Rollup window
1. InfluxDB instance path
1. Database name
1. Retention policy name
1. Retention policy

### Example for routing metadata
```
{
  "tenantIdAndMeasurement": "tenant_id_1:cpu",
  "routes": {
    "60m": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_155d",
      "retentionPolicy": "155d"
    },
    "1440m": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_1825d",
      "retentionPolicy": "1825d"
    },
    "5m": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_10d",
      "retentionPolicy": "10d"
    },
    "240m": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_300d",
      "retentionPolicy": "300d"
    },
    "full": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_5d",
      "retentionPolicy": "5d"
    },
    "20m": {
      "path": "http://localhost:8086",
      "databaseName": "db_2",
      "retentionPolicyName": "rp_20d",
      "retentionPolicy": "20d"
    }
  }
}
```
## Setup
- install Redis locally (can be found here [https://redis.io/download])
- Run `redis-server` from the command line to start redis
- You can access redis with `redis-cli`

If you have docker installed, alternatively, you can use [`test-infrastructure`](https://github.com/racker/ceres-test-infrastructure) repository to install and run `Kafka`, `InfluxDB` and `Redis`. Please follow instruction from that repository to install them.

## API
1. Add routing information - This endpoint provides the routing information. If routing information does not exist, it will create routing information and provides that information back to the caller.
   - URL - `http://localhost:8080/{tenantId}/{measurement}`
   - RequestMethod: `GET`
   - Request example: `http://localhost:8081/tenant_id_1/cpu`
2. Query only routing information - This endpoint only allows query operation for the given tenantId and measurement. It doesn't create new routes if tenantId and measurement combination is new to the system.
   - URL - `http://localhost:8080/{tenantId}/{measurement}/?readOnly=true`
   - RequestMethod: `GET`
   - Request example: `http://localhost:8081/tenant_id_3/cpu?readOnly=true`
3. Get all of the measurements for given tenantId
   - URL - `http://localhost:8080/{tenantId}/measurements`
   - RequestMethod: `GET`
   - Request example: `http://localhost:8081/tenant_id_1/measurements`
