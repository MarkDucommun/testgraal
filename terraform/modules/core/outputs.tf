output "registry_name" {
  value = azurerm_container_registry.registry.name
}

output "registry_login_server" {
  value = azurerm_container_registry.registry.login_server
}

output "registry_admin_username" {
  value = azurerm_container_registry.registry.admin_username
}

output "registry_admin_password" {
  value = azurerm_container_registry.registry.admin_password
}

output "resource_group_name" {
  value = var.resource_group_name
}

output "location" {
  value = var.location
}

output "storage_account_name" {
  value = azurerm_storage_account.testgraal-storage.name
}

output "storage_account_access_key" {
  value = azurerm_storage_account.testgraal-storage.primary_access_key
}

output "service_plan_id" {
  value = azurerm_service_plan.testgraal-service-plan.id
}
