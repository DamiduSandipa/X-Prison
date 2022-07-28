package dev.drawethree.ultraprisoncore.gangs.api.events;

import dev.drawethree.ultraprisoncore.gangs.model.Gang;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangCreateEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	@Getter
	private final CommandSender creator;

	@Getter
	private final Gang gang;

	/**
	 * Fired when gang is created
	 *
	 * @param creator CommandSender who created the gang
	 * @param gang    Gang
	 */
	public GangCreateEvent(CommandSender creator, Gang gang) {
		this.creator = creator;
		this.gang = gang;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
