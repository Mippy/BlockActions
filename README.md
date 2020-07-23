# BlockActions

Simple light-weight plugin to manage blocks' actions on right-click.

### Commands

`/ba add <Material> <Command>` - Defines a new block action for the given material. MUST match Material types as documented by the [Enum on the Spigot-API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html). Commands should not include the preceding `/`. Permissions are ignored on block actions.\
`/ba remove <Material>` - Removes the block action you set-up with `/ba add`.\
`/ba disable <Material>` - Disable's a block's action entirely. Denied message configurable in config.\
`/ba list` - Lists all block actions.

All commands above require OP or `blockactions.manage`.

### config.yml

```
DeniedMsg: "Input custom denied msg here. Chat colors supported with &"
```