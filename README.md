# Tenant Routing Service
## Responsibilities
- Tenant white-listing. Any tenant data that's not here in the list will error out into the log, so that we know if we are trying to push unexpected tenant data into InfluxDB.
- Capacity tracking for tenants. This will assist into our decision to see which InfluxDB instance particular tenant will go to. And in case of (which will very well happen) we see that a given tenant needs more series, we might have to update their location accordingly.
- In future, we can add "admin" UI on top of this service to add/update tenant routing or capacity related metadata.
- Tenant series count. This will assist in capacity planning or tenant data relocation.

## Requirements
- Prepopulate Redis datastore with tenant routing metadata
- Get routing metadata

## Routing Metadata
Routing metadata should contain following items:
1. Tenant Id
1. Rollup window
1. InfluxDB instance path
1. Database name
1. Retention policy name
1. Retention policy
1. Projected Max series count for the given tenant <br /> <br />
Max series count (i.e. 1K, 10K, or 100K). It’s more like S, M, L (Small, Medium, Large) <br />
**NOTE:** Right now, we are going with only two buckets (>=100K and <100K)

### Example for routing metadata
```
{
	"tenantId": "hybrid:12345",
	"routes" : {
		"full" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_5d",
			"retentionPolicy": "5d",
			"maxSeriesCount": 100000
		},
		"5m" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_10d",
			"retentionPolicy": "10d",
			"maxSeriesCount": 100000
		},
		"20m" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_20d",
			"retentionPolicy": "20d",
			"maxSeriesCount": 100000
		},
		"60m" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_155d",
			"retentionPolicy": "155d",
			"maxSeriesCount": 100000
		},
		"240m" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_300d",
			"retentionPolicy": "300d",
			"maxSeriesCount": 100000
		},
		"1440m" : {
			"path": "http://data-influxdb:8086",
			"databaseName": "db_hybrid_12345",
			"retentionPolicyName": "rp_1825d",
			"retentionPolicy": "1825d",
			"maxSeriesCount": 100000
		}
	}
}
```
## Setup
- install Redis locally (can be found here [https://redis.io/download])
- Run `redis-server` from the command line to start redis
- You can access redis with `redis-cli`