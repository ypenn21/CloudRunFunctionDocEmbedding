# CloudRunFunctionDocEmbedding

gcloud cli deployment:

gcloud functions deploy document-embeddings --gen2 --region=us-central1 --entry-point=services.DocumentEmbeddingFunction --runtime=java21 --trigger-http --source=. --vpc-connector alloy-connector
