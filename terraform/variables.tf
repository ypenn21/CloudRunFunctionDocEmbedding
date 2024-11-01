variable "cloud_function_list_project" {
  description = "Value of the name for the Cloud Function to list Project Ids to be scanned"
  type        = string
}

variable "cloud_function_list_project_desc" {
  description = "Value of the description for the Cloud Function to list Project Ids to be scanned"
  type        = string
}

variable "region" {
  description = "cloud function region"
  type        = string
}

variable "project_id" {
  description = "project id"
  type        = string
}


variable "cloud_function_list_project_memory" {
  description = "Value of the memory for the Cloud Function to list Project Ids to be scanned"
  type        = number
  default     = 512
}

variable "bucket_gcf_source" {
  description = "Value of the cloud storage bucket where the source code is"
  type        = string
}

variable "source_code_object" {
  description = "Value of source code artifact"
  type        = string
}

variable "service_account_email" {
  description = "Value of the Service Account"
  type        = string
}

variable "cloud_function_list_project_timeout" {
  description = "Value of the timeout for the Cloud Function to list Project Ids to be scanned"
  type        = number
  default     = 540
}

variable "db_uri" {
  description = "Value of alloydb uri with private ip and port"
  type        = string
}
variable "db_password" {
  description = "Value of alloydb password"
  type        = string
}
variable "db_user" {
  description = "Value of alloydb user"
  type        = string
}
variable "db_name" {
  description = "Value of alloydb db name"
  type        = string
}