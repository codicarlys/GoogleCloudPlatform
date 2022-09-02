# GoogleCloudPlatform
Repository with Google Cloud Solutions

Dataflow
A solution to read data from oracle database a write to Cloud Storage as Avro file

## Hou to create a dataflow template
To create a template after you clone the repository go to the dataflow folder
cd dataflow/

Execute the following command
The following fields with "<parameter_name> should be altered conform your need.
```shell
mvn compile exec:java \
-Pdataflow-runner \
-Dexec.mainClass=com.google.cloud.teleport.templates.JdbcToAvro \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args=" \
--project=<projet_id> \
--stagingLocation=gs://<bucket>/staging \
--tempLocation=gs://<bucket>/temp \
--templateLocation=gs://<bucket>/templates/JdbcToAvro.json \
--runner=DataflowRunner --region=<region>"
```
* bucket = bucket that you created
* region = region where you created the bucket

## To Execute the template
The following fields with "<parameter_name> should be altered conform your need.
```shell
gcloud dataflow jobs run <dataflow-job-name> \
--gcs-location gs://<bucket>/templates/JdbcToAvro.json \
--region <master_region> \
--worker-zone <worker_region> \
--service-account-email <service account> \
--staging-location gs://<bucket>/staging \
--subnetwork <subnetwork> \
--network <network> \
--additional-experiments disable_runner_v2_reason=template \
--disable-public-ips \
--parameters ^~^url="<jdbc_url>"~driverClass=<datbase_driver>~output=<output_path>~username=<username>
```
* dataflow-job-name = The name of dataflow job  
* bucket = Bucket path to template file  
* master_region = The region of main server   
* worker_region = The region of the worker servers   
* service_account = The service account with permission to access the database and bucket  
* network = It is the network where problably your vpn with the database is accessable  
* subnetwork = Same as the network option  

Then we have the parameters of the database. So in this case this template contemplate an example of connection to a oracle database, so if you have another database to use it will be necessary to update the pom file with the correct library and alter the jdbc_url and database_driver parameters. This said  
* jdbc_url = jdbc:oracle:thin:@(description=(address=(host=127.0.0.1)(protocol=tcp)(port=1521))(connect_data=(SERVICE_NAME=teste)))  
* database_driver = oracle.jdbc.driver.OracleDriver
