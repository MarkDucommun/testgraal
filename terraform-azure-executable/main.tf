resource "azurerm_resource_group" "testgraal_rg" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_storage_account" "testgraal_storage" {
  name                     = "storagetestgraal"
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_service_plan" "testgraal_service_plan" {
  name                = "asp-functionapp-testgraal"
  resource_group_name = var.resource_group_name
  location            = var.location
  os_type             = "Linux"
  sku_name = "Y1"
}

resource "azurerm_linux_function_app" "functionapp" {
  name                = var.function_app_name
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = azurerm_service_plan.testgraal_service_plan.id
  storage_account_name       = azurerm_storage_account.testgraal_storage.name
  storage_account_access_key = azurerm_storage_account.testgraal_storage.primary_access_key

  app_settings = {
    FUNCTIONS_WORKER_RUNTIME     = "custom" # Required for native executables
    WEBSITES_PORT                = "8080"  # Your app listens on this port
    AzureWebJobsStorage          = azurerm_storage_account.testgraal_storage.primary_connection_string
    FUNCTION_APP_EDIT_MODE       = "readonly" # Ensure read-only mode for runtime
  }

  site_config {
  }
}
