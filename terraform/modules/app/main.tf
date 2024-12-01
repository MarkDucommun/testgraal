resource "azurerm_linux_function_app" "functionapp" {
  name                = "functionapp-testgraal"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = var.service_account_id
  storage_account_name = var.storage_account_name
  storage_account_access_key = var.storage_account_access_key

  app_settings = {
    FUNCTIONS_WORKER_RUNTIME = "custom"
    WEBSITES_PORT            = "8080"
    DOCKER_REGISTRY_SERVER_URL = var.registry_login_server
    DOCKER_REGISTRY_SERVER_USERNAME = var.registry_admin_username
    DOCKER_REGISTRY_SERVER_PASSWORD = var.registry_admin_password
  }

  site_config {
    always_on = true
    linux_fx_version = "DOCKER|${var.registry_login_server}/testgraal-image:${var.image_tag}"
  }
}
