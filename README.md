# CloudRunFunctionDocEmbedding

gcloud cli deployment:

gcloud functions deploy document-embeddings --set-env-vars='alloydb_uri=ip:5432,alloydb_user=postgres,alloydb_password=pword,alloydb_db=dbname' --gen2 --region=us-central1 --entry-point=services.DocumentEmbeddingFunction --runtime=java21 --trigger-http --source=. --vpc-connector alloy-connector


Cloud console test:

{
"fileName": "Poems-Yanni_Shakespeare-1989-public.txt"
}