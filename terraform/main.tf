provider "google" {
  project = var.project_id  # Replace with your GCP project ID
  region  = var.region
}

resource "google_compute_network" "auto_vpc" {
  name = "default"
  auto_create_subnetworks = true # This creates subnets in each region, similar to a default VPC
}

resource "google_compute_router" "router" {
  depends_on = [google_compute_network.auto_vpc]
  project = var.project_id
  name    = "nat-router"
  network = google_compute_network.auto_vpc.name
  region  = var.region
}

resource "google_vpc_access_connector" "alloy_connector" {
  name          = "alloy-connector"
  region        = var.region
  network       = "default"
  ip_cidr_range = "10.100.0.0/28"
  depends_on = [google_compute_network.auto_vpc]
}

resource "google_cloudfunctions_function" "function-listProjects" {
  name        = var.cloud_function_list_project
  description = var.cloud_function_list_project_desc
  runtime     = "java21"
  region      = var.region

  available_memory_mb   = var.cloud_function_list_project_memory
  source_archive_bucket = var.bucket_gcf_source
  source_archive_object = var.source_code_object
  trigger_http          = true
  entry_point           = "functions.ListProjects"
  service_account_email = var.service_account_email
  timeout               = var.cloud_function_list_project_timeout

  environment_variables = {
    alloydb_uri = var.db_uri
    alloydb_password  = var.db_password
    alloydb_user = var.db_user
    alloydb_db = var.db_name
  }

  depends_on = [google_vpc_access_connector.alloy_connector]
}