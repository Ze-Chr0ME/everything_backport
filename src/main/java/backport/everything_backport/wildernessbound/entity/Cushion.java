package backport.everything_backport.wildernessbound.entity;

import backport.everything_backport.wildernessbound.WildBlocks;
import backport.everything_backport.wildernessbound.WildItems;
import backport.everything_backport.wildernessbound.WildSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * 26.3's cushion: a seat that sits on top of whatever flat surface it was placed on. It doesn't collide, can't be pushed, and pops off
 * when its support goes or when it is buried in suffocating blocks.
 */
public class Cushion extends BlockAttachedEntity {
	private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
	private static final int CHECK_INTERVAL = 100;
	private static final double ANCHOR_DEPTH = 0.015625;
	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Cushion.class, EntityDataSerializers.INT);
	private int ticksSinceLastCheck;

	public Cushion(EntityType<? extends Cushion> type, Level level) {
		super(type, level);
	}

	public DyeColor getColor() {
		return DyeColor.byId(this.entityData.get(DATA_COLOR));
	}

	public void setColor(DyeColor color) {
		this.entityData.set(DATA_COLOR, color.getId());
	}

	@Override
	public void dropItem(ServerLevel level, @Nullable Entity causedBy) {
		this.playSound(WildSounds.CUSHION_BREAK, 1.0F, 1.0F);
		this.showBreakingParticles();
		if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			if (!(causedBy instanceof Player player && player.hasInfiniteMaterials())) {
				this.spawnAtLocation(level, this.getCushionItemStackWithData());
			}
		}
	}

	private static boolean isBreakingDeniedFor(DamageSource source) {
		return source.getEntity() instanceof Player player && !player.mayBuild();
	}

	private boolean isBreakingDeniedAtPosFor(ServerLevel level, DamageSource source) {
		return source.getEntity() instanceof Player player && !level.mayInteract(player, this.pos);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		return !isBreakingDeniedFor(source) && !this.isBreakingDeniedAtPosFor(level, source) && super.hurtServer(level, source, damage);
	}

	@Override
	public boolean hurtClient(DamageSource source) {
		return !isBreakingDeniedFor(source) && super.hurtClient(source);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive() || this.isVehicle()) {
			return InteractionResult.PASS;
		} else if (!this.level().isClientSide() && player.startRiding(this)) {
			this.playSound(WildSounds.CUSHION_SIT, 1.0F, 1.0F);
			return InteractionResult.SUCCESS_SERVER;
		} else {
			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (!this.level().isClientSide() && this.getRemovalReason() == null) {
			this.playSound(WildSounds.CUSHION_GET_UP, 1.0F, 1.0F);
		}
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(WildItems.CUSHIONS.get(this.getColor()));
	}

	@Override
	public void tick() {
		super.tick();
		// 26.3's tickAtCheckInterval: cushions burn up in fire and lava.
		if (this.level() instanceof ServerLevel level && this.ticksSinceLastCheck++ >= CHECK_INTERVAL) {
			this.ticksSinceLastCheck = 0;
			if (!this.isRemoved() && level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
				this.hurtServer(level, this.damageSources().lava(), 4.0F);
			}

			this.destroyIfInFire(level);
		}
	}

	public void destroyIfInFire(ServerLevel level) {
		if (!this.isRemoved()) {
			for (BlockPos blockPos : BlockPos.betweenClosed(BlockPos.containing(this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ), BlockPos.containing(this.getBoundingBox().maxX, this.getBoundingBox().maxY, this.getBoundingBox().maxZ))) {
				if (level.getBlockState(blockPos).is(BlockTags.FIRE)) {
					this.hurtServer(level, this.damageSources().inFire(), 1.0F);
					return;
				}
			}
		}
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
		if (!this.isRemoved()) {
			this.kill(level);
			this.dropItem(level, lightningBolt);
		}
	}

	@Override
	public void setPos(double x, double y, double z) {
		this.setPosRaw(x, y, z);
		super.setPos(x, y, z);
	}

	private void showBreakingParticles() {
		if (this.level() instanceof ServerLevel level) {
			level.sendParticles(
				new BlockParticleOption(ParticleTypes.BLOCK, WildBlocks.wool(this.getColor()).defaultBlockState()),
				this.getX(),
				this.getY(0.6666666666666666),
				this.getZ(),
				10,
				this.getBbWidth() / 4.0F,
				this.getBbHeight() / 4.0F,
				this.getBbWidth() / 4.0F,
				0.05
			);
		}
	}

	public static boolean canBePlacedAt(Level level, AABB boundingBox) {
		return wouldSurviveAt(level, boundingBox) && !isAnchorBuried(level, boundingBox);
	}

	public static boolean wouldSurviveAt(Level level, AABB boundingBox) {
		return hasAnchorBelow(level, boundingBox) && !isCoveredBySuffocatingBlocks(level, boundingBox);
	}

	private static boolean hasAnchorBelow(Level level, AABB boundingBox) {
		AABB anchorBox = new AABB(
			boundingBox.minX, boundingBox.minY - ANCHOR_DEPTH, boundingBox.minZ, Math.nextDown(boundingBox.maxX), boundingBox.minY, Math.nextDown(boundingBox.maxZ)
		);
		AABB searchBox = anchorBox.expandTowards(0.0, -0.125, 0.0);
		for (BlockPos blockPos : BlockPos.betweenClosed(
			BlockPos.containing(searchBox.minX, searchBox.minY, searchBox.minZ), BlockPos.containing(searchBox.maxX, searchBox.maxY, searchBox.maxZ)
		)) {
			BlockState blockState = level.getBlockState(blockPos);
			VoxelShape shape = blockState.getShape(level, blockPos);
			if (!shape.isEmpty() && shape.bounds().move(blockPos).intersects(anchorBox)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isAnchorBuried(Level level, AABB boundingBox) {
		AABB restingSlice = deflated(
			new AABB(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY + ANCHOR_DEPTH, boundingBox.maxZ)
		);
		VoxelShape exposedSurface = Shapes.create(restingSlice);

		for (VoxelShape collider : level.getBlockCollisions(null, restingSlice)) {
			exposedSurface = Shapes.join(exposedSurface, collider, BooleanOp.ONLY_FIRST);
			if (exposedSurface.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	private static boolean isCoveredBySuffocatingBlocks(Level level, AABB boundingBox) {
		AABB box = deflated(boundingBox);
		for (BlockPos blockPos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
			if (!level.getBlockState(blockPos).isSuffocating(level, blockPos)) {
				return false;
			}
		}

		return true;
	}

	/** 26.3's {@code AABB.nextDeflated}: shrinks a box by the smallest step so touching neighbors don't count. */
	private static AABB deflated(AABB box) {
		return new AABB(
			Math.nextUp(box.minX), Math.nextUp(box.minY), Math.nextUp(box.minZ), Math.nextDown(box.maxX), Math.nextDown(box.maxY), Math.nextDown(box.maxZ)
		);
	}

	@Override
	public boolean survives() {
		return wouldSurviveAt(this.level(), this.getBoundingBox());
	}

	@Override
	protected void recalculateBoundingBox() {
		this.setBoundingBox(this.getDimensions(this.getPose()).makeBoundingBox(this.position()));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DATA_COLOR, DEFAULT_COLOR.getId());
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.store("color", DyeColor.CODEC, this.getColor());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setColor(input.read("color", DyeColor.CODEC).orElse(DEFAULT_COLOR));
	}

	private ItemStack getCushionItemStackWithData() {
		ItemStack itemStack = new ItemStack(WildItems.CUSHIONS.get(this.getColor()));
		itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
		return itemStack;
	}
}
