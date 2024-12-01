resource "azurerm_resource_group" "testgraal-rg" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_container_registry" "registry" {
  name                = "acrtestgraal"
  resource_group_name = var.resource_group_name
  location            = var.location
  sku                 = "Basic"
  admin_enabled       = true
}

resource "azurerm_storage_account" "testgraal-storage" {
  name                     = "storagetestgraalmed"
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_service_plan" "testgraal-service-plan" {
  name                = "asp-functionapp-testgraal"
  resource_group_name = var.resource_group_name
  location            = var.location
  os_type             = "Linux"
  sku_name = "Y1"
}
