output "testgraal_registry_name" {
  value = azurerm_container_registry.registry.name
}

output "testgraal_registry_login_server" {
  value = azurerm_container_registry.registry.login_server
}
