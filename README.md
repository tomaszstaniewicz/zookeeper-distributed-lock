## Example of distributed lock in zookeeper
```shell script
docker-compose -f docker/docker-compose.yml up
```

Then: run DistributedLockDemo.java for several times (as separate processed)
Only one of processes should acquire lock

   
