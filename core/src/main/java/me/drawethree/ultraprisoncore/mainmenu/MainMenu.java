package me.drawethree.ultraprisoncore.mainmenu;

import me.drawethree.ultraprisoncore.UltraPrisonCore;
import me.drawethree.ultraprisoncore.mainmenu.reload.ReloadSelectionGui;
import me.drawethree.ultraprisoncore.mainmenu.reset.ResetSelectionGui;
import me.drawethree.ultraprisoncore.utils.PlayerUtils;
import me.drawethree.ultraprisoncore.utils.SkullUtils;
import me.drawethree.ultraprisoncore.utils.compat.CompMaterial;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.text3.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainMenu extends Gui {

	private static final MenuScheme LAYOUT_WHITE = new MenuScheme()
			.mask("011111110")
			.mask("110000011")
			.mask("100000001")
			.mask("110000011")
			.mask("011111110");

	private static final MenuScheme LAYOUT_RED = new MenuScheme()
			.mask("100000001")
			.mask("000000000")
			.mask("000000000")
			.mask("000000000")
			.mask("100000001");

	private static final MenuScheme CONTENT = new MenuScheme()
			.mask("000000000")
			.mask("000111000")
			.mask("001111100")
			.mask("000111000")
			.mask("000000000");
	private final UltraPrisonCore core;

	public MainMenu(UltraPrisonCore core, Player player) {
		super(player, 5, "UltraPrisonCore - Main Menu");
		this.core = core;
	}

	@Override
	public void redraw() {
		if (isFirstDraw()) {

			MenuPopulator populator = LAYOUT_WHITE.newPopulator(this);

			while (populator.hasSpace()) {
				populator.accept(ItemStackBuilder.of(CompMaterial.WHITE_STAINED_GLASS_PANE.toItem()).name(" ").buildItem().build());
			}

			populator = LAYOUT_RED.newPopulator(this);

			while (populator.hasSpace()) {
				populator.accept(ItemStackBuilder.of(CompMaterial.RED_STAINED_GLASS_PANE.toItem()).name(" ").buildItem().build());
			}
		}

		//Information
		this.setItem(13, ItemStackBuilder.of(SkullUtils.INFO_SKULL.clone()).name("&e&lInformation").lore("&7Author: &f" + StringUtils.join(this.core.getDescription().getAuthors(), ", "), "&7Version: &f" + this.core.getDescription().getVersion()).build(() -> {

		}));

		//Reload
		this.setItem(21, ItemStackBuilder.of(SkullUtils.COMMAND_BLOCK_SKULL.clone()).name("&e&lReload Modules").lore("&7Click to reload specific module").build(() -> {
			if (!this.getPlayer().hasPermission("ultraprisoncore.mainmenu.reload")) {
				return;
			}
			this.close();
			new ReloadSelectionGui(this.core, this.getPlayer()).open();
		}));

		//Debug
		this.setItem(22, ItemStackBuilder.of(this.core.isDebugMode() ? SkullUtils.CHECK_SKULL.clone() : SkullUtils.CROSS_SKULL.clone()).name("&e&lDebug Mode: " + (this.core.isDebugMode() ? "&2&lON" : "&c&lOFF")).lore("&7Click to toggle debug mode.").build(() -> {
			if (!this.getPlayer().hasPermission("ultraprisoncore.mainmenu.debug")) {
				return;
			}
			this.core.setDebugMode(!this.core.isDebugMode());
			this.redraw();
		}));

		//Reset Data
		this.setItem(23, ItemStackBuilder.of(SkullUtils.DANGER_SKULL.clone()).name("&e&lReset Player Data").lore("&7Click to select which module data", "&7would you like to wipe.").build(() -> {
			if (!this.getPlayer().hasPermission("ultraprisoncore.mainmenu.reset")) {
				return;
			}
			this.close();
			new ResetSelectionGui(this.core, this.getPlayer()).open();
		}));

		this.setItem(36, ItemStackBuilder.of(Material.BARRIER).name("&c&lClose").lore("&7Click to close the gui.").build(this::close));
		this.setItem(44, ItemStackBuilder.of(SkullUtils.HELP_SKULL.clone()).name("&e&lNeed more help?").lore("&7Right-Click to see plugin's Wiki", "&7Left-Click to join Discord Support.")
				.build(() -> {
					this.close();
					PlayerUtils.sendMessage(this.getPlayer(), " ");
					PlayerUtils.sendMessage(this.getPlayer(), Text.colorize("&eUltraPrisonCore - Wiki"));
					PlayerUtils.sendMessage(this.getPlayer(), Text.colorize("&7https://github.com/Drawethree/UltraPrisonCore/wiki"));
					PlayerUtils.sendMessage(this.getPlayer(), " ");
				}, () -> {
					this.close();
					PlayerUtils.sendMessage(this.getPlayer(), " ");
					PlayerUtils.sendMessage(this.getPlayer(), Text.colorize("&eUltraPrisonCore - Discord"));
					PlayerUtils.sendMessage(this.getPlayer(), Text.colorize("&7https://discord.com/invite/cssWTNK"));
					PlayerUtils.sendMessage(this.getPlayer(), " ");
				}));

	}
}
