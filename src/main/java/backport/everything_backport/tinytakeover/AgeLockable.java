package backport.everything_backport.tinytakeover;

/** A mob a golden dandelion can stop from growing up: ageable mobs and tadpoles in 26.1. */
public interface AgeLockable {
	boolean everythingBackport$isAgeLocked();

	void everythingBackport$setAgeLocked(boolean locked);

	/** Ticks left of the particle burst shown after the lock changes; the dandelion can't be used again until it ends. */
	int everythingBackport$getAgeLockParticleTimer();

	/** Flips the lock, restarts growth from the start and begins the particle burst. */
	void everythingBackport$toggleAgeLock();

	/** Whether the mob counts as young for the golden dandelion; tadpoles always do. */
	boolean everythingBackport$canBeAgeLocked();
}
