variable "resource_group_name" {
  description = "Name of the resource group for the testgraal app"
  type        = string
}

variable "location" {
  description = "Azure region where resources will be created"
  type        = string
  default     = "East US"
}
