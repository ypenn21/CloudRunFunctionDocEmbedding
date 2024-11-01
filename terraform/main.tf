provider "google" {
  project = "your_project_id"  # Replace with your GCP project ID
  region  = "us-central1"
}

resource "google_cloudfunctions2_function" "document_embeddings" {
  name        = "document-embeddings"
  location    = "us-central1"
  runtime     = "java21"
  service_config {
      environment_variables = {
        GOOGLE_RUNTIME_VERSION = "21"
      }
      ingress_settings = "ALLOW_ALL"  # Optional, adjust based on your security requirements
      vpc_connector    = "projects/your_project_id/locations/us-central1/connectors/alloy-connector"  # Replace with your connector name
  }

  build_config {
    entry_point = "services.DocumentEmbeddingFunction"
    runtime     = "java21"
    source      = {
      storage_source {
        bucket = "your-source-bucket"  # Replace with your GCS bucket for the source code
        object = "source.zip"          # Path to the uploaded source in the bucket
      }
    }
  }

  event_trigger {
    trigger_http = true
  }
}