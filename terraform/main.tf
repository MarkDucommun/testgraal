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
  name                     = "functionappstoragetestgraal"
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

resource "azurerm_linux_function_app" "functionapp" {
  name                = "functionapp-testgraal"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = azurerm_service_plan.testgraal-service-plan.id
  storage_account_name = azurerm_storage_account.testgraal-storage.name
  storage_account_access_key = azurerm_storage_account.testgraal-storage.primary_access_key

  app_settings = {
    FUNCTIONS_WORKER_RUNTIME = "custom"
    WEBSITES_PORT            = "8080"
    DOCKER_REGISTRY_SERVER_URL = azurerm_container_registry.registry.login_server
    DOCKER_REGISTRY_SERVER_USERNAME = azurerm_container_registry.registry.admin_username
    DOCKER_REGISTRY_SERVER_PASSWORD = azurerm_container_registry.registry.admin_password
  }

  site_config {
    always_on = true
    linux_fx_version = "DOCKER|${azurerm_container_registry.registry.login_server}/testgraal-image:${var.image_tag}"
  }
}
