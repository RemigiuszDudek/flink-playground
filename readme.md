# Flink commands
`flink/bin/start-cluster.bat` - starts single job manager and one task manager

# Flink console
Console start on `http://localhost:8081/#/overview`

# Deploy job using REST API
* Upload job jar file
```
d:\Projects\flink-playground\build\libs>curl
    -X POST
    -H "Expect:"
    -F "jarfile=@d:\Projects\flink-playground\build\libs\flink-playground-1.0-SNAPSHOT.jar" http://localhost:8081/jars/upload
{
    "filename": "C:\Users\Admin\AppData\Local\Temp\flink-web-9b8d004e-25a9-429e-8938-77a35ae2a67d\flink-web-upload\bbb31d37-e7ef-4f06-ba49-ba6e0e62d100_flink-playground-1.0-SNAPSHOT.jar",
    "status":   "success"
}
```

* list uploaded jar files to get the `id`
```
d:\Projects\flink-playground\build\libs>curl -X GET http://localhost:8081/jars
{
    "address": "http://localhost:8081",
    "files": [
        {
            "id":       "bbb31d37-e7ef-4f06-ba49-ba6e0e62d100_flink-playground-1.0-SNAPSHOT.jar",
            "name":     "flink-playground-1.0-SNAPSHOT.jar",
            "uploaded": 1554760923081,
            "entry":    [
                {
                    "name":         "prv.dudekre.flink.CollateralValuationJob",
                    "description":  null
                }
            ]
        }
    ]
}
```

* run job given the `id`
```
d:\Projects\flink-playground\build\libs>curl -X POST http://localhost:8081/jars/bbb31d37-e7ef-4f06-ba49-ba6e0e62d100_flink-playground-1.0-SNAPSHOT.jar/run
{
    "jobid": "f73aee261b62050c8b5fa2d6dbe2589f"
}
```

