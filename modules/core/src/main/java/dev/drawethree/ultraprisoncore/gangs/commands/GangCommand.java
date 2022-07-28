package dev.drawethree.ultraprisoncore.gangs.commands;

import dev.drawethree.ultraprisoncore.gangs.UltraPrisonGangs;
import dev.drawethree.ultraprisoncore.gangs.commands.impl.*;
import dev.drawethree.ultraprisoncore.gangs.commands.impl.admin.GangAdminSubCommand;
import dev.drawethree.ultraprisoncore.gangs.commands.impl.value.GangValueSubCommand;
import dev.drawethree.ultraprisoncore.gangs.gui.panel.GangPanelGUI;
import dev.drawethree.ultraprisoncore.gangs.model.Gang;
import dev.drawethree.ultraprisoncore.utils.player.PlayerUtils;
import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GangCommand {

	@Getter
	private final UltraPrisonGangs plugin;
	private final Map<String, GangSubCommand> subCommands;

	public GangCommand(UltraPrisonGangs plugin) {
		this.plugin = plugin;
		this.subCommands = new HashMap<>();
	}

	private void registerSubCommands() {
		registerSubCommand(new GangHelpSubCommand(this));
		registerSubCommand(new GangInfoSubCommand(this));
		registerSubCommand(new GangCreateSubCommand(this));
		registerSubCommand(new GangInviteSubCommand(this));
		registerSubCommand(new GangAcceptSubCommand(this));
		registerSubCommand(new GangLeaveSubCommand(this));
		registerSubCommand(new GangDisbandSubCommand(this));
		registerSubCommand(new GangKickSubCommand(this));
		registerSubCommand(new GangTopSubCommand(this));
		registerSubCommand(new GangAdminSubCommand(this));
		registerSubCommand(new GangValueSubCommand(this));
		registerSubCommand(new GangRenameSubCommand(this));
		registerSubCommand(new GangChatSubCommand(this));
	}

	public void register() {
		this.registerSubCommands();
		this.registerMainCommand();
	}

	private void registerMainCommand() {
		Commands.create()
				.tabHandler(this::createTabHandler)
				.handler(c -> {

					if (c.args().size() == 0) {

						if (c.sender() instanceof Player) {
							Optional<Gang> optionalGang = this.getPlugin().getGangsManager().getPlayerGang((Player) c.sender());
							optionalGang.ifPresent(gang -> openGangPanelGui(gang, (Player) c.sender()));
							return;
						}

					}

					GangSubCommand subCommand = this.getSubCommand(Objects.requireNonNull(c.rawArg(0)));

					if (subCommand != null) {

						if (!subCommand.canExecute(c.sender())) {
							PlayerUtils.sendMessage(c.sender(), this.plugin.getConfig().getMessage("no-permission"));
							return;
						}

						subCommand.execute(c.sender(), c.args().subList(1, c.args().size()));
					} else {
						this.getHelpSubCommand().execute(c.sender(), c.args());
					}
				}).registerAndBind(this.plugin.getCore(), this.plugin.getConfig().getGangsCommandAliases());
	}

	private GangSubCommand getHelpSubCommand() {
		return getSubCommand("help");
	}

	private List<String> createTabHandler(CommandContext<CommandSender> context) {

		if (context.args().size() == 0) {
			return new ArrayList<>(this.subCommands.keySet());
		}

		GangSubCommand subCommand = getSubCommand(context.rawArg(0));

		if (subCommand != null) {
			return subCommand.getTabComplete();
		}

		return new ArrayList<>(this.subCommands.keySet());
	}

	private void registerSubCommand(GangSubCommand command) {
		for (String alias : command.getAliases()) {
			this.subCommands.put(alias.toLowerCase(), command);
		}
	}

	private GangSubCommand getSubCommand(String arg) {
		return subCommands.get(arg.toLowerCase());
	}

	private void openGangPanelGui(Gang gang, Player player) {
		new GangPanelGUI(this.plugin, gang, player).open();
	}
}
