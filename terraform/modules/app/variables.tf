variable "image_tag" {
  description = "Tag for the Docker image"
  type        = string
  default     = "latest"
}

variable "resource_group_name" {
  description = "Name of the resource group for the testgraal app"
  type        = string
}

variable "location" {
  description = "Azure region where resources will be created"
  type        = string
  default     = "East US"
}

variable "registry_admin_username" {
  description = "Admin username for the container registry"
  type        = string
}

variable "registry_admin_password" {
  description = "Admin password for the container registry"
  type        = string
}

variable "registry_login_server" {
  description = "Login server for the container registry"
  type        = string
}

variable "storage_account_name" {
  type = string
}

variable "storage_account_access_key" {
  type = string
}

variable "service_account_id" {
  type = string
}
