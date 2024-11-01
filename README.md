# CloudRunFunctionDocEmbedding

gcloud cli deployment:

gcloud functions deploy document-embeddings --set-env-vars='alloydb_uri=10.127.4.2:5432,alloydb_user=postgres,alloydb_password=37ni7sSyUEj5Ffb!!!!!5432111NGBIGGUYYANNI,alloydb_db=library' --gen2 --region=us-central1 --entry-point=services.DocumentEmbeddingFunction --runtime=java21 --trigger-http --source=. --vpc-connector alloy-connector
