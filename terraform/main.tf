module "core" {
  source = "./modules/core"
  resource_group_name = var.resource_group_name
  location = var.location
}

module "app" {
  source = "./modules/app"

  resource_group_name        = module.core.resource_group_name
  location                   = module.core.location
  registry_login_server      = module.core.registry_login_server
  registry_admin_username    = module.core.registry_admin_username
  registry_admin_password    = module.core.registry_admin_password
  storage_account_name       = module.core.storage_account_name
  storage_account_access_key = module.core.storage_account_access_key
  service_account_id         = module.core.service_plan_id
}
