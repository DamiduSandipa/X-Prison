package dev.drawethree.ultraprisoncore.enchants.model.impl;

import dev.drawethree.ultraprisoncore.enchants.UltraPrisonEnchants;
import dev.drawethree.ultraprisoncore.enchants.model.UltraPrisonEnchantment;
import dev.drawethree.ultraprisoncore.gangs.UltraPrisonGangs;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public final class GangValueFinderEnchant extends UltraPrisonEnchantment {

	private double chance;
	private String amountToGiveExpression;


	public GangValueFinderEnchant(UltraPrisonEnchants instance) {
		super(instance, 23);
		this.amountToGiveExpression = plugin.getEnchantsConfig().getYamlConfig().getString("enchants." + id + ".Amount-To-Give");
		this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
	}

	@Override
	public void onEquip(Player p, ItemStack pickAxe, int level) {

	}

	@Override
	public void onUnequip(Player p, ItemStack pickAxe, int level) {

	}

	@Override
	public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {
		double chance = getChanceToTrigger(enchantLevel);
		if (chance >= ThreadLocalRandom.current().nextDouble(100)) {
			if (!this.plugin.getCore().isModuleEnabled(UltraPrisonGangs.MODULE_NAME)) {
				return;
			}
			int amount = (int) createExpression(enchantLevel).evaluate();
			plugin.getCore().getGangs().getGangsManager().getPlayerGang(e.getPlayer()).ifPresent(gang -> gang.setValue(gang.getValue() + amount));
		}
	}

	@Override
	public double getChanceToTrigger(int enchantLevel) {
		return chance * enchantLevel;
	}

	@Override
	public void reload() {
		super.reload();
		this.amountToGiveExpression = plugin.getEnchantsConfig().getYamlConfig().getString("enchants." + id + ".Amount-To-Give");
		this.chance = plugin.getEnchantsConfig().getYamlConfig().getDouble("enchants." + id + ".Chance");
	}

	private Expression createExpression(int level) {
		return new ExpressionBuilder(this.amountToGiveExpression)
				.variables("level")
				.build()
				.setVariable("level", level);
	}

	@Override
	public String getAuthor() {
		return "Drawethree";
	}
}