config = require('load_prototypes_config')

print('factorio_path = ' .. config.factorio_path)

-- add paths
local package_paths = {
  '/data/core/lualib',
  '/data/base'
}

local factorio_package_path = ''

for _, package_path in ipairs(package_paths) do
  factorio_package_path = factorio_package_path .. config.factorio_path .. package_path .. '/?.lua;';
end

package.path = factorio_package_path .. package.path

-- load modules
local module_names = {
  'dataloader',

  'prototypes.recipe.ammo',
  'prototypes.recipe.capsule',
  'prototypes.recipe.demo-furnace-recipe',
  'prototypes.recipe.demo-recipe',
  'prototypes.recipe.demo-turret',
  'prototypes.recipe.equipment',
  'prototypes.recipe.fluid-recipe',
  'prototypes.recipe.furnace-recipe',
  'prototypes.recipe.inserter',
  'prototypes.recipe.module',
  'prototypes.recipe.recipe',
  'prototypes.recipe.turret',

  'prototypes.entity.demo-doodads',
  'prototypes.entity.demo-entities',
  'prototypes.entity.demo-mining-drill',
  'prototypes.entity.demo-remnants',
  'prototypes.entity.demo-resources',
  'prototypes.entity.demo-trees',
  'prototypes.entity.demo-turrets',
  'prototypes.entity.enemies',
  'prototypes.entity.entities',
  'prototypes.entity.mining-drill',
  'prototypes.entity.resources',
  'prototypes.entity.turrets',
  'prototypes.equipment.equipment',
  'prototypes.fluid.demo-fluid',
  'prototypes.fluid.fluid',
  'prototypes.item.ammo',
  'prototypes.item.armor',
  'prototypes.item.capsule',
  'prototypes.item.demo-ammo',
  'prototypes.item.demo-armor',
  'prototypes.item.demo-gun',
  'prototypes.item.demo-item',
  'prototypes.item.demo-item-groups',
  'prototypes.item.demo-mining-tools',
  'prototypes.item.demo-turret',
  'prototypes.item.equipment',
  'prototypes.item.gun',
  'prototypes.item.item',
  'prototypes.item.mining-tools',
  'prototypes.item.module',
  'prototypes.item.turret',
  'prototypes.recipe.fluid-recipe',
  'prototypes.technology.bullet-upgrades',
  'prototypes.technology.chemistry',
  'prototypes.technology.combat-robots',
  'prototypes.technology.equipment',
  'prototypes.technology.gun-turret-upgrades',
  'prototypes.technology.laser-turret-upgrades',
  'prototypes.technology.logistic-robot',
  'prototypes.technology.module',
  'prototypes.technology.rocket-upgrades',
  'prototypes.technology.shotgun-shell-upgrades',
  'prototypes.technology.technology'
}

for _, module_name in ipairs(module_names) do
  require(module_name)
end

-- serialize json
data.extend = nil
data.clear = nil

local json = require ("dkjson")

str = json.encode (data, { indent = true })

local file = io.open("prototypes.json", "w")
file:write(str)
